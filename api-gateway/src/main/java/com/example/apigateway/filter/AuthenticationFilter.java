package com.example.apigateway.filter;

import com.example.apigateway.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final String BLACK_LIST_KEY = "members:%s:black_list";

    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthenticationFilter(JwtProvider jwtProvider, RedisTemplate<String, Object> redisTemplate) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (DoNotFindAuthorizationHeader(request)) {
                return onError(exchange, "Authorzation Header가 존재하지 않습니다.");
            }

            String token = getTokenFromAuthorizationHeader(request);
            String memberId = jwtProvider.getMemberIdFromToken(token);
            if (memberId == null) {
                return onError(exchange, "잘못된 토큰입니다.");
            }

            if (tokenRegisteredInBlackList(token, memberId)) {
                return onError(exchange, "사용할 수 없는 토큰입니다.");
            }

            ServerHttpRequest updatedRequest = request.mutate()
                    .header("X-Member-Id", memberId)
                    .build();

            exchange = exchange.mutate().request(updatedRequest).build();
            return chain.filter(exchange);
        };
    }

    private boolean tokenRegisteredInBlackList(String token, String memberId) {
        String key = String.format(BLACK_LIST_KEY, memberId);

        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return false;
        }

        return value.toString().equals("Bearer " + token);
    }

    private static boolean DoNotFindAuthorizationHeader(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(AUTHORIZATION);
    }

    private static String getTokenFromAuthorizationHeader(ServerHttpRequest request) {
        return Objects.requireNonNull(request.getHeaders().getFirst(AUTHORIZATION))
                .replace("Bearer ", "");
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.setStatusCode(UNAUTHORIZED);
        log.info("API GATEWAY ERROR: {}", message);
        return serverHttpResponse.setComplete();
    }

    static class Config {
    }
}
