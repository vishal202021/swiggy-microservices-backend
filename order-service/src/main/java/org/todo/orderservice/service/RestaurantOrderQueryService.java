package org.todo.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.todo.orderservice.client.RestaurantClient;
import org.todo.orderservice.dto.OrderResponse;
import org.todo.orderservice.helper.ToResponse;
import org.todo.orderservice.model.Orders;
import org.todo.orderservice.repo.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantOrderQueryService {
    private final OrderRepository orderRepository;
    private final RestaurantClient restaurantClient;

    public List<OrderResponse> getRestaurantOrders(Long restaurantId,
                                                   Long userId,
                                                   String authorization){
        Long ownerId=restaurantClient.getOwnerId(restaurantId,authorization);
        if(!ownerId.equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Not restaurant owner");
        }
        List<Orders> orders=orderRepository.findByRestaurantId(restaurantId);
        List<OrderResponse> orderResponseList=new ArrayList<OrderResponse>();
        for(Orders order:orders){
            OrderResponse orderResponse= ToResponse.toOrderResponse(order);
            orderResponseList.add(orderResponse);
        }
        return orderResponseList;
    }

}
