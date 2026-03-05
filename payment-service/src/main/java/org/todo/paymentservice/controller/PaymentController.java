package org.todo.paymentservice.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.todo.paymentservice.dto.InitiatePaymentRequest;
import org.todo.paymentservice.dto.PaymentResponse;
import org.todo.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiate(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody InitiatePaymentRequest request
    ){
        return ResponseEntity.ok().body(paymentService.initiate(userId,authorization,idempotencyKey,request));
    }

    @PostMapping("{paymentId}/success")
    public ResponseEntity<Void> success(
            @PathVariable Long paymentId,
            @RequestHeader("Authorization") String authorization
    ){
        paymentService.success(paymentId,authorization);
        return ResponseEntity.ok().build();
    }
}
