package org.todo.paymentservice.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.todo.paymentservice.client.OrderClient;
import org.todo.paymentservice.dto.InitiatePaymentRequest;
import org.todo.paymentservice.dto.OrderDto;
import org.todo.paymentservice.dto.OrderStatus;
import org.todo.paymentservice.dto.PaymentResponse;
import org.todo.paymentservice.model.Payment;
import org.todo.paymentservice.model.PaymentStatus;
import org.todo.paymentservice.repo.PaymentRepository;
import org.todo.paymentservice.toResponse.ToResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    public PaymentResponse initiate(
            Long userId,
            String authorization,
            String idempotencyKey,
            InitiatePaymentRequest request
    ) {

        Optional<Payment> existing =
                paymentRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            return ToResponse.toResponse(existing.get(),razorpayKeyId);
        }

        if (paymentRepository.findByOrderIdAndStatus(
                request.getOrderId(), PaymentStatus.INITIATED).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Payment already in progress");
        }

        OrderDto orderDto =
                orderClient.getOrder(request.getOrderId(), authorization);

        if (!orderDto.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Order does not belong to user");
        }

        if (orderDto.getStatus() != OrderStatus.CREATED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Order already paid or completed"
            );
        }

        try {

            JSONObject options = new JSONObject();
            options.put("amount",
                    orderDto.getTotalAmount().multiply(BigDecimal.valueOf(100)));
            options.put("currency", "INR");
            options.put("receipt", "order_" + orderDto.getOrderId());

            Order razorpayOrder = razorpayClient.orders.create(options);


            Payment payment = new Payment();
            payment.setOrderId(orderDto.getOrderId());
            payment.setUserId(userId);
            payment.setAmount(orderDto.getTotalAmount());
            payment.setProvider("RAZORPAY");
            payment.setStatus(PaymentStatus.INITIATED);
            payment.setTransactionId(razorpayOrder.get("id"));
            payment.setIdempotencyKey(idempotencyKey);
            payment.setCreatedAt(LocalDateTime.now());

            Payment saved = paymentRepository.save(payment);

            return ToResponse.toResponse(saved,razorpayKeyId);

        } catch (RazorpayException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Failed to create Razorpay order"
            );
        }
    }



    public void success(Long paymentId, String authorization) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));


        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }


        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);


        orderClient.confirmOrder(
                payment.getOrderId(),
                payment.getUserId(),
                authorization
        );
    }

}
