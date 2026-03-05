package org.todo.orderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
public class RestaurantClient {
    private final RestTemplate restTemplate;

    @Value("${restaurant.service.url}")
    private String restaurantUrl;

    public Long getOwnerId(Long restaurantId,String authorization) {
      String url=restaurantUrl+"/restaurants/"+restaurantId+"/owner";
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.AUTHORIZATION,authorization);
      HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Long> response = restTemplate
                .exchange(url, HttpMethod.GET, entity, Long.class);
        return response.getBody();
    }
}
