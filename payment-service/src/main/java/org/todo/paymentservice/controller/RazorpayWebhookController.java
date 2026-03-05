package org.todo.paymentservice.controller;

import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.todo.paymentservice.model.Payment;
import org.todo.paymentservice.model.PaymentStatus;
import org.todo.paymentservice.repo.PaymentRepository;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class RazorpayWebhookController {

    @Value("${razorpay.webhook-secret}")
    private String webhookSecret;

    private final PaymentRepository paymentRepository;

    @PostMapping("/payments/webhook/razorpay")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {

        try {
            boolean valid = Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    webhookSecret
            );

            if (!valid) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid Razorpay webhook signature"
                );
            }

            JSONObject event = new JSONObject(payload);
            String eventType = event.getString("event");

            JSONObject paymentEntity =
                    event.getJSONObject("payload")
                            .getJSONObject("payment")
                            .getJSONObject("entity");

            String razorpayOrderId = paymentEntity.getString("order_id");

            Payment payment = paymentRepository
                    .findByTransactionId(razorpayOrderId)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Payment not found for Razorpay order"
                            ));


            if ("payment.captured".equals(eventType)) {
                if (payment.getStatus() != PaymentStatus.SUCCESS) {
                    payment.setStatus(PaymentStatus.SUCCESS);
                    payment.setUpdatedAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                }
            }


            if ("payment.failed".equals(eventType)) {
                if (payment.getStatus() != PaymentStatus.FAILED) {
                    payment.setStatus(PaymentStatus.FAILED);
                    payment.setUpdatedAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                }
            }

            return ResponseEntity.ok().build();

        } catch (RazorpayException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Webhook verification failed"
            );
        }
    }
}
