package org.todo.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.todo.orderservice.client.MenuClient;
import org.todo.orderservice.client.RestaurantClient;
import org.todo.orderservice.dto.*;
import org.todo.orderservice.helper.ToOrderDTO;
import org.todo.orderservice.helper.ToResponse;
import org.todo.orderservice.model.*;
import org.todo.orderservice.repo.OrderItemRepository;
import org.todo.orderservice.repo.OrderRepository;
import org.todo.orderservice.repo.OutBoxRepo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuClient menuClient;
    private final RestaurantClient restaurantClient;
    private final OutBoxRepo outBoxRepo;


    @Transactional
    public OrderResponse placeOrder(
            Long userId,
            PlaceOrderRequest request,
            String authorization
    ) {
        Orders order = new Orders();
        order.setUserId(userId);
        order.setRestaurantId(request.getRestaurantId());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO);

        orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {

            MenuItemDto menuItem =
                    menuClient.getMenuItem(itemReq.getMenuItemId(), authorization);

            if (!menuItem.getRestaurantId().equals(request.getRestaurantId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Menu item does not belong to restaurant"
                );
            }

            if (!menuItem.getAvailable()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Menu item not available"
                );
            }

            BigDecimal itemTotal =
                    menuItem.getPrice()
                            .multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            total = total.add(itemTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setMenuItemId(itemReq.getMenuItemId());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(menuItem.getPrice());

            orderItemRepository.save(orderItem);
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        saveOutboxEvent(order);

        return ToResponse.toOrderResponse(order);
    }


    @Transactional
    public OrderResponse updateStatus(
            Long orderId,
            Long userId,
            OrderStatus nextStatus,
            String authorization
    ) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
                );

        OrderStatus current = order.getStatus();

        if (nextStatus == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Status is required"
            );
        }

        if (current == OrderStatus.CANCELLED ||
                current == OrderStatus.DELIVERED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Order already in terminal state"
            );
        }

        if (nextStatus == OrderStatus.PAID ||
                nextStatus == OrderStatus.CANCELLED) {

            if (!order.getUserId().equals(userId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Not your order"
                );
            }
        }

        if (nextStatus == OrderStatus.CONFIRMED ||
                nextStatus == OrderStatus.PREPARING ||
                nextStatus == OrderStatus.DELIVERED) {

            Long ownerId = restaurantClient.getOwnerId(
                    order.getRestaurantId(), authorization);

            if (!ownerId.equals(userId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Not restaurant owner"
                );
            }
        }

        if (!OrderStateTransition.isValid(current, nextStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid order status transition"
            );
        }

        order.setStatus(nextStatus);
        orderRepository.save(order);

        saveOutboxEvent(order);

        return ToResponse.toOrderResponse(order);
    }


    public OrderDto getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
                );
        return ToOrderDTO.toOrderDto(order);
    }


    private void saveOutboxEvent(Orders order) {

        OrderStatusEvent event = new OrderStatusEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(order.getId());
        event.setUserId(order.getUserId());
        event.setTotalAmount(order.getTotalAmount());
        event.setStatus(order.getStatus().toString());

        OutboxEvent outbox = OutboxEvent.from(event);
        outBoxRepo.save(outbox);
    }
}
