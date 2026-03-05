package org.todo.apigateway.jwtfilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InternalTokenValidator {
    @Value("${internal.tokens.notification-service}")
    private String notificationServiceToken;

    @Value("${internal.tokens.order-service}")
    private String orderServiceToken;

    public boolean isValid(String token) {
        return token != null &&
                (token.equals(notificationServiceToken)
                        || token.equals(orderServiceToken));
    }
}
