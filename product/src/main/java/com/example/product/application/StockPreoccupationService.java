package com.example.product.application;

import com.example.product.application.port.StockPreoccupationRepository;
import com.example.product.domain.PreoccupiedStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void release(Long orderId) {
        stockPreoccupationRepository.deleteAllByOrderId(orderId);
    }

    public List<PreoccupiedStock> findAllByOrderId(Long orderId) {
        return stockPreoccupationRepository.findAllByOrderId(orderId);
    }

}
