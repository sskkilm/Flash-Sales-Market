package com.example.order.application;

import com.example.order.application.port.CacheRepository;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.exception.OrderServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheRepository cacheRepository;

    public void decreaseStock(List<OrderProduct> orderProducts) {
        Map<Long, Integer> successfullyDecreased = new HashMap<>();

        try {
            decreaseStockCache(orderProducts, successfullyDecreased);
        } catch (OrderServiceException e) {
            rollbackStockCache(successfullyDecreased);
            throw e;
        }
    }

    private void decreaseStockCache(List<OrderProduct> orderProducts, Map<Long, Integer> successfullyDecreased) {
        orderProducts
                .forEach(orderProduct -> {
                    Long result = cacheRepository.decreaseStock(
                            orderProduct.getProductId(), orderProduct.getQuantity()
                    );
                    log.info("{}번 상품에 남은 재고:{}", orderProduct.getProductId(), result);
                    successfullyDecreased.put(orderProduct.getProductId(), orderProduct.getQuantity());
                });
    }

    private void rollbackStockCache(Map<Long, Integer> successfullyDecreased) {
        successfullyDecreased
                .forEach((productId, quantity) -> {
                    Long result = cacheRepository.increaseStock(productId, quantity);
                    log.info("{}번 상품에 복구된 재고:{}, 총 재고:{}", productId, quantity, result);
                });
    }
}
