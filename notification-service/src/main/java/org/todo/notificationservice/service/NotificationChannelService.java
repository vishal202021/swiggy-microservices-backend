package org.todo.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.todo.notificationservice.client.UserClient;
import org.todo.notificationservice.dto.UserContactDto;

@Service
@RequiredArgsConstructor
public class NotificationChannelService {

    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final UserClient userClient;

    private static final Logger log =
            LoggerFactory.getLogger(NotificationChannelService.class);

    public void sendOrderCreatedNotification(Long userId, Long orderId, String status) {

        log.info("NotificationChannelService called for userId={}, orderId={}, status={}",
                userId, orderId, status);


        UserContactDto user = userClient.getUser(userId);

        String message = buildMessage(orderId, status);

        log.info("User contact fetched: email={}, phone={}, message={}",
                user.getEmail(), user.getPhone(),message);
        emailService.sendEmail(
                user.getEmail(),
                "Order Created",
                message
        );
        whatsAppService.sendWhatsApp(
                user.getPhone()
                , message);
    }

    private String buildMessage(Long orderId, String status) {
        return switch (status) {
            case "CREATED" -> "Your order #" + orderId + " has been created successfully";
            case "PAID" -> "Payment received for order #" + orderId;
            case "CANCELLED" -> "Your order #" + orderId + " has been cancelled";
            case "CONFIRMED" -> "Restaurant confirmed your order #" + orderId;
            case "PREPARING" -> "Your food is being prepared ";
            case "DELIVERED" -> "Your order #" + orderId + " has been delivered. Enjoy!";
            default -> "Order #" + orderId + " status updated";
        };
    }
}

