package org.todo.paymentservice.controller;

import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.todo.paymentservice.client.OrderClient;
import org.todo.paymentservice.dto.VerifyPaymentRequest;
import org.todo.paymentservice.model.Payment;
import org.todo.paymentservice.model.PaymentStatus;
import org.todo.paymentservice.repo.PaymentRepository;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentVerificationController {

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPayment(
            @RequestBody VerifyPaymentRequest request,
            @RequestHeader("Authorization") String authorization
    ) {



        try {

            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpayOrderId());
            options.put("razorpay_payment_id", request.getRazorpayPaymentId());
            options.put("razorpay_signature", request.getRazorpaySignature());

            boolean valid = Utils.verifyPaymentSignature(options, razorpaySecret);

            if (!valid) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment signature");
            }

            Payment payment = paymentRepository
                    .findByTransactionId(request.getRazorpayOrderId())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));


            if (payment.getStatus() != PaymentStatus.SUCCESS) {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);

                orderClient.confirmOrder(
                        payment.getOrderId(),
                        payment.getUserId(),
                        authorization
                );
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                   e.getMessage()
            );
        }
    }
}
