package com.example.product.application;

import com.example.product.domain.HoldStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoldStockService {

    private final HoldStockRepository holdStockRepository;

    public int getHoldStockQuantityInProduct(Long productId) {
        List<HoldStock> holdStocks = holdStockRepository.findAllByProductId(productId);
        return holdStocks.stream().mapToInt(HoldStock::getQuantity).sum();
    }

    public void hold(Long orderId, Long productId, int quantity) {
        HoldStock holdStock = HoldStock.create(orderId, productId, quantity);
        holdStockRepository.save(holdStock);
    }

    public void release(Long orderId) {
        holdStockRepository.deleteAllByOrderId(orderId);
    }

    public List<HoldStock> findAllHoldStockByOrderId(Long orderId) {
        return holdStockRepository.findAllByOrderId(orderId);
    }

    @Transactional
    public void releaseInvalidHoldingStock() {
        // TODO
    }
}
