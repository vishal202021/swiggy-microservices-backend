package org.todo.paymentservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.todo.paymentservice.dto.OrderDto;
import org.todo.paymentservice.dto.OrderStatus;


@Component
@RequiredArgsConstructor
public class OrderClient {

    private final RestTemplate restTemplate;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    public OrderDto getOrder(Long orderId,String authorization){
        HttpHeaders  headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,authorization);
        HttpEntity<OrderDto> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                orderServiceUrl + "/orders/" + orderId,
                HttpMethod.GET,
                entity,
                OrderDto.class
        ).getBody();
    }

    public void confirmOrder(Long orderId, Long userId, String authorization) {

        String url = "http://localhost:8080/orders/"+orderId+"/status";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", String.valueOf(userId));
        headers.set("Authorization", authorization);

        OrderDto body = new OrderDto();
        body.setStatus(OrderStatus.CONFIRMED);

        HttpEntity<OrderDto> request = new HttpEntity<>(body, headers);

        restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                request,
                Void.class
        );
    }





}
