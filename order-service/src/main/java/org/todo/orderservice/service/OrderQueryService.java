package org.todo.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.todo.orderservice.dto.OrderResponse;
import org.todo.orderservice.helper.ToResponse;
import org.todo.orderservice.model.Orders;
import org.todo.orderservice.repo.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderResponse> getMyOrders(Long userUd){
        List<Orders> orders = orderRepository.findByUserId(userUd);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Orders order:orders){
            OrderResponse orderResponse = ToResponse.toOrderResponse(order);
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
}
