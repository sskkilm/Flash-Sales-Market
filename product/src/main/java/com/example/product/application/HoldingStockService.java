package com.example.product.application;

import com.example.product.domain.HoldingStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoldingStockService {

    private final HoldingStockRepository holdingStockRepository;

    public int getHoldingStockQuantityInProduct(Long productId) {
        List<HoldingStock> holdingStocks = holdingStockRepository.findAllByProductId(productId);
        return holdingStocks.stream().mapToInt(HoldingStock::getQuantity).sum();
    }

    public void create(Long orderId, Long productId, int quantity) {
        HoldingStock holdingStock = HoldingStock.create(orderId, productId, quantity);
        holdingStockRepository.save(holdingStock);
    }

    public void release(Long orderId) {
        holdingStockRepository.deleteAllByOrderId(orderId);
    }

    public List<HoldingStock> findAllByOrderId(Long orderId) {
        return holdingStockRepository.findAllByOrderId(orderId);
    }

    @Transactional
    public void releaseInvalidHoldingStock() {
        // TODO
    }
}
