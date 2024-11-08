package com.example.product.application;

import com.example.product.domain.HoldingStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoldingStockService {

    private final HoldingStockRepository holdingStockRepository;

    public int getHoldingStockQuantity(Long productId) {
        List<HoldingStock> holdingStocks = holdingStockRepository.findAllByProductId(productId);
        return holdingStocks.stream().mapToInt(HoldingStock::getQuantity).sum();
    }

    public void create(Long orderId, Long productId, int quantity) {
        HoldingStock holdingStock = HoldingStock.create(orderId, productId, quantity);
        holdingStockRepository.save(holdingStock);
    }
}
