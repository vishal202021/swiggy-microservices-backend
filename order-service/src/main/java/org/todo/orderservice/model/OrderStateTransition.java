package org.todo.orderservice.model;

import java.util.Arrays;

public enum OrderStateTransition {
    CREATED_TO_PAID(OrderStatus.CREATED, OrderStatus.PAID),
    CREATED_TO_CANCELLED(OrderStatus.CREATED, OrderStatus.CANCELLED),
    PAID_TO_CONFIRMED(OrderStatus.PAID, OrderStatus.CONFIRMED),
    CONFIRMED_TO_PREPARING(OrderStatus.CONFIRMED, OrderStatus.PREPARING),
    PREPARING_TO_DELIVERED(OrderStatus.PREPARING, OrderStatus.DELIVERED);

    private final OrderStatus from;
    private final OrderStatus to;

    OrderStateTransition(OrderStatus from, OrderStatus to) {
        this.from = from;
        this.to = to;
    }


    public static boolean isValid(OrderStatus from, OrderStatus to) {
        return Arrays.stream(values())
                .anyMatch(t -> t.from == from && t.to == to);
    }
}
