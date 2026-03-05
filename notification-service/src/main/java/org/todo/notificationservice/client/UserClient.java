package org.todo.notificationservice.client;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.todo.notificationservice.dto.UserContactDto;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(UserClient.class);

    @Value("${user.service.url}")
    public String url;

    @Value("${internal.token.notification-service}")
    public String token;

    public UserContactDto getUser(Long userId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-INTERNAL-TOKEN",token);
        HttpEntity<Void> entity= new HttpEntity<>(headers);

        ResponseEntity<UserContactDto> response=
                restTemplate.exchange(
                        url+"/internal/users/"+userId+"/contact",
                        HttpMethod.GET,
                        entity,
                        UserContactDto.class
                );
        logger.info("getUser:{}",response.getBody());
        return response.getBody();

    }


}
