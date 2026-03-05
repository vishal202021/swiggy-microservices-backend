package org.todo.paymentservice.toResponse;
import org.todo.paymentservice.dto.PaymentResponse;
import org.todo.paymentservice.model.Payment;

public class ToResponse {


    public static PaymentResponse toResponse(Payment payment,String razorpayKeyId) {
        PaymentResponse paymentResponse=new PaymentResponse();
        paymentResponse.setPaymentId(payment.getId());
        paymentResponse.setOrderId(payment.getOrderId());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setStatus(payment.getStatus());
        paymentResponse.setRazorpayKey(razorpayKeyId);
        paymentResponse.setRazorpayOrderId(payment.getTransactionId());
        return paymentResponse;
    }
}
