package org.todo.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.todo.orderservice.dto.OrderDto;
import org.todo.orderservice.dto.OrderResponse;
import org.todo.orderservice.dto.PlaceOrderRequest;
import org.todo.orderservice.dto.UpdateOrderStatusRequest;
import org.todo.orderservice.service.OrderQueryService;
import org.todo.orderservice.service.OrderService;
import org.todo.orderservice.service.RestaurantOrderQueryService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;
    private final RestaurantOrderQueryService restaurantOrderQueryService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("Authorization") String authorization,
            @RequestBody PlaceOrderRequest placeOrderRequest
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(userId, placeOrderRequest,authorization));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("Authorization") String authorization,
            @RequestBody OrderDto orderDto
            ){
        return ResponseEntity.ok()
                .body(orderService.updateStatus(orderId,
                        userId,
                        orderDto.getStatus(),
                        authorization
                ));
    }

   @GetMapping("/my")
   public ResponseEntity<List<OrderResponse>> getMyOrders(
           @RequestHeader("X-User-ID") Long userId
   ) {
        return ResponseEntity.ok().body(orderQueryService.getMyOrders(userId));
   }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("Authorization") String authorization) {

        return ResponseEntity.ok(
                restaurantOrderQueryService.getRestaurantOrders(
                        restaurantId, userId, authorization));
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        OrderDto orderDto=orderService.getOrder(orderId);
        return ResponseEntity.ok().body(orderDto);
    }
}
