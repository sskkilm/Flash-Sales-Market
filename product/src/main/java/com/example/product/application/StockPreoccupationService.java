package com.example.product.application;

import com.example.product.application.port.StockPreoccupationRepository;
import com.example.product.domain.PreoccupiedStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockPreoccupationService {

    private final StockPreoccupationRepository stockPreoccupationRepository;

    public int getPreoccupiedStockQuantityInProduct(Long productId) {
        List<PreoccupiedStock> preoccupiedStocks = stockPreoccupationRepository.findAllByProductId(productId);
        return preoccupiedStocks.stream().mapToInt(PreoccupiedStock::getQuantity).sum();
    }

    public void preoccupy(Long orderId, Long productId, int quantity) {
        PreoccupiedStock preoccupiedStock = PreoccupiedStock.create(orderId, productId, quantity);
        stockPreoccupationRepository.save(preoccupiedStock);
    }

    @Transactional
    public void release(Long orderId) {
        stockPreoccupationRepository.deleteAllByOrderId(orderId);
        log.info("Order Id:{} 선점 재고 해제", orderId);
    }

    public List<PreoccupiedStock> findAllByOrderId(Long orderId) {
        return stockPreoccupationRepository.findAllByOrderId(orderId);
    }

}
