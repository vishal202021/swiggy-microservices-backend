package org.todo.paymentservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.todo.paymentservice.model.Payment;
import org.todo.paymentservice.model.PaymentStatus;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    Optional<Payment> findByOrderIdAndStatus(Long orderId, PaymentStatus paymentStatus);


    Optional<Payment> findByTransactionId(String transactionId);
}
