package com.example.product.application;

import com.example.product.aop.DistributedLock;
import com.example.product.application.port.StockPreoccupationRepository;
import com.example.product.domain.PreoccupiedStock;
import com.example.product.domain.Product;
import com.example.product.dto.StockPreoccupationInfo;
import com.example.product.dto.StockPreoccupationResult;
import com.example.product.exception.ProductServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.product.exception.error.ErrorCode.NOT_OPENED;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockPreoccupationService {

    private final StockPreoccupationRepository stockPreoccupationRepository;

    @DistributedLock
    @Transactional
    public List<StockPreoccupationResult> preoccupy(Long orderId, Map<Long, Product> productMap, List<StockPreoccupationInfo> stockPreoccupationInfos) {

        List<PreoccupiedStock> preoccupiedStocks = new ArrayList<>();

        List<StockPreoccupationResult> results = stockPreoccupationInfos.stream()
                .map(info -> process(
                        orderId, productMap.get(info.productId()), info.quantity(), preoccupiedStocks
                )).toList();

        stockPreoccupationRepository.saveAll(preoccupiedStocks);
        return results;
    }

    private StockPreoccupationResult process(Long orderId, Product product, int quantity, List<PreoccupiedStock> preoccupiedStocks) {
        if (product.isLimited() && product.isNotOpened()) {
            throw new ProductServiceException(NOT_OPENED);
        }

        int preoccupiedStockQuantity = stockPreoccupationRepository.findQuantitiesByProductId(product.getId()).stream().mapToInt(e -> e).sum();
        log.info("{}번 상품에 선점되어 있는 재고: {}", product.getId(), preoccupiedStockQuantity);
        product.checkOutOfStock(quantity, preoccupiedStockQuantity);

        preoccupiedStocks.add(PreoccupiedStock.create(orderId, product.getId(), quantity));

        return new StockPreoccupationResult(product.getId(), product.getName(), quantity, product.getPrice());
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
