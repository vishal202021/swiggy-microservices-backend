package org.todo.orderservice.client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.todo.orderservice.dto.MenuItemDto;



@Component
@RequiredArgsConstructor
public class MenuClient {
    private final RestTemplate restTemplate;

    @Value("${menu.service.url}")
    private String menuServiceUrl;

    public MenuItemDto getMenuItem(Long menuItemId,String authorization){
        String url=menuServiceUrl+"/menus/"+menuItemId;
        HttpHeaders headers=new HttpHeaders();
        headers.set("Authorization",authorization);
        HttpEntity<Void> entity=new HttpEntity<>(headers);
        ResponseEntity<MenuItemDto> response=restTemplate.exchange(url, HttpMethod.GET,entity,MenuItemDto.class);
        return response.getBody();
    }


}
