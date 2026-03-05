package org.todo.apigateway.jwtfilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component

public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final String SECRET =
            "SWIGGY_SECRET_KEY_123SWIGGY_SECRET_KEY_123";

    private final Key key = Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8)
    );

    private final InternalTokenValidator internalTokenValidator;

    public  JwtAuthFilter(InternalTokenValidator internalTokenValidator) {
        this.internalTokenValidator = internalTokenValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("JwtAuthFilter HIT: " +
                exchange.getRequest().getURI());
        String path = exchange.getRequest().getURI().getPath();

        if(path.startsWith("/internal/")){
            String internalToken=exchange.getRequest().getHeaders().getFirst("X-INTERNAL-TOKEN");
            if(!internalTokenValidator.isValid(internalToken)){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        }

        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }
        if (path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            Long userId = claims.get("id", Long.class);
            String role = claims.get("role", String.class);


            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .headers(headers -> {
                        headers.remove("X-User-Id");
                        headers.remove("X-User-Email");
                        headers.remove("X-User-Role");
                        headers.add("X-User-Id", String.valueOf(userId));
                        headers.add("X-User-Email", email);
                        headers.add("X-User-Role", role);
                    })
                    .build();


            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
