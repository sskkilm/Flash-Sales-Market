package com.example.product.application;

import com.example.product.domain.LimitedProduct;
import com.example.product.domain.NormalProduct;
import com.example.product.domain.Product;
import com.example.product.domain.ProductType;
import com.example.product.dto.*;
import com.example.product.exception.InsufficientStockException;
import com.example.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void 상품_목록을_조회한다() {
        //given
        Product product1 = NormalProduct.builder()
                .id(1L)
                .name("name1")
                .price(new BigDecimal("10000"))
                .build();
        Product product2 = LimitedProduct.builder()
                .id(2L)
                .name("name2")
                .price(new BigDecimal("20000"))
                .build();

        given(productRepository.findAll())
                .willReturn(List.of(product1, product2));

        //when
        List<ProductDto> productList = productService.getProductList();

        //then
        assertEquals(2, productList.size());

        ProductDto productDto1 = productList.get(0);
        assertEquals(1L, productDto1.productId());
        assertEquals("name1", productDto1.name());
        assertEquals(ProductType.NORMAL.name(), productDto1.type());
        assertEquals(new BigDecimal("10000"), productDto1.price());

        ProductDto productDto2 = productList.get(1);
        assertEquals(2L, productDto2.productId());
        assertEquals("name2", productDto2.name());
        assertEquals(ProductType.LIMITED.name(), productDto2.type());
        assertEquals(new BigDecimal("20000"), productDto2.price());
    }

    @Test
    void 존재하지_않는_상품의_상세_정보를_조회하면_예외가_발생한다() {
        //given
        given(productRepository.findById(1L))
                .willThrow(new ProductNotFoundException(1L));

        //then
        assertThrows(ProductNotFoundException.class,
                // when
                () -> productService.getProductDetails(1L));
    }

    @Test
    void 일반_상품의_상세_정보를_조회한다() {
        //given
        LocalDateTime createdAt = LocalDateTime.of(
                2024, 11, 2, 12, 0, 0
        );
        LocalDateTime updatedAt = LocalDateTime.of(
                2024, 11, 2, 12, 0, 0
        );
        Product product = NormalProduct.builder()
                .id(1L)
                .name("name")
                .price(new BigDecimal("10000"))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        given(productRepository.findById(1L))
                .willReturn(product);

        //when
        ProductDetails productDetails = productService.getProductDetails(1L);

        //then
        assertEquals(1L, productDetails.productId());
        assertEquals("name", productDetails.name());
        assertEquals(new BigDecimal("10000"), productDetails.price());
        assertEquals(ProductType.NORMAL.name(), productDetails.type());
        assertEquals(createdAt, productDetails.createdAt());
        assertEquals(updatedAt, productDetails.updatedAt());
    }

    @Test
    void 한정판_상품의_상세_정보를_조회한다() {
        //given
        LocalDateTime createdAt = LocalDateTime.of(
                2024, 11, 2, 12, 0, 0
        );
        LocalDateTime updatedAt = LocalDateTime.of(
                2024, 11, 2, 12, 0, 0
        );
        Product product = LimitedProduct.builder()
                .id(1L)
                .name("name")
                .price(new BigDecimal("10000"))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        given(productRepository.findById(1L))
                .willReturn(product);

        //when
        ProductDetails productDetails = productService.getProductDetails(1L);

        //then
        assertEquals(1L, productDetails.productId());
        assertEquals("name", productDetails.name());
        assertEquals(new BigDecimal("10000"), productDetails.price());
        assertEquals(ProductType.LIMITED.name(), productDetails.type());
        assertEquals(createdAt, productDetails.createdAt());
        assertEquals(updatedAt, productDetails.updatedAt());
    }

    @Test
    void 존재하지_않는_상품을_구매하면_예외가_발생한다() {
        //given
        ProductPurchaseRequest productPurchaseRequest = new ProductPurchaseRequest(
                List.of(
                        new ProductPurchaseInfo(1L, 1)
                )
        );

        given(productRepository.findById(1L))
                .willThrow(new ProductNotFoundException(1L));

        //then
        assertThrows(ProductNotFoundException.class,
                //when
                () -> productService.purchase(productPurchaseRequest));
    }

    @Test
    void 상품_구매_시_재고가_부족하면_예외가_발생한다() {
        //given
        ProductPurchaseRequest productPurchaseRequest = new ProductPurchaseRequest(
                List.of(
                        new ProductPurchaseInfo(1L, 1)
                )
        );

        given(productRepository.findById(1L))
                .willReturn(
                        NormalProduct.builder()
                                .stockQuantity(0)
                                .build()
                );

        //then
        assertThrows(InsufficientStockException.class,
                //when
                () -> productService.purchase(productPurchaseRequest));
    }

    @Test
    void 상품을_구매한다() {
        //given
        ProductPurchaseRequest productPurchaseRequest = new ProductPurchaseRequest(
                List.of(
                        new ProductPurchaseInfo(1L, 1),
                        new ProductPurchaseInfo(2L, 2)
                )
        );

        Product product1 = NormalProduct.builder()
                .id(1L)
                .price(new BigDecimal("10000"))
                .stockQuantity(10)
                .name("name1")
                .build();
        Product product2 = LimitedProduct.builder()
                .id(2L)
                .price(new BigDecimal("20000"))
                .stockQuantity(10)
                .name("name2")
                .build();
        given(productRepository.findById(1L))
                .willReturn(product1);
        given(productRepository.findById(2L))
                .willReturn(product2);

        //when
        ProductPurchaseResponse response = productService.purchase(productPurchaseRequest);

        //then
        assertEquals(8, product2.getStockQuantity());
        assertEquals(9, product1.getStockQuantity());

        List<PurchasedProductInfo> purchasedProductPurchaseFeignRespons = response.purchasedProductInfos();
        assertEquals(2, purchasedProductPurchaseFeignRespons.size());

        PurchasedProductInfo purchasedProductInfo1 = purchasedProductPurchaseFeignRespons.get(0);
        assertEquals(1L, purchasedProductInfo1.productId());
        assertEquals(1, purchasedProductInfo1.quantity());
        assertEquals("name1", purchasedProductInfo1.productName());
        assertEquals(new BigDecimal("10000"), purchasedProductInfo1.purchaseAmount());

        PurchasedProductInfo purchasedProductInfo2 = purchasedProductPurchaseFeignRespons.get(1);
        assertEquals(2L, purchasedProductInfo2.productId());
        assertEquals(2, purchasedProductInfo2.quantity());
        assertEquals("name2", purchasedProductInfo2.productName());
        assertEquals(new BigDecimal("40000"), purchasedProductInfo2.purchaseAmount());
    }

    @Test
    void 존재하지_않는_상품의_재고를_복구하면_예외가_발생한다() {
        //given
        ProductRestoreStockRequest productRestoreStockRequest = new ProductRestoreStockRequest(
                List.of(
                        new ProductRestoreStockInfo(1L, 1),
                        new ProductRestoreStockInfo(2L, 2)
                )
        );

        given(productRepository.findById(1L))
                .willThrow(new ProductNotFoundException(1L));

        //then
        assertThrows(ProductNotFoundException.class,
                // when
                () -> productService.restoreStock(productRestoreStockRequest));
    }

    @Test
    void 상품_재고를_복구한다() {
        //given
        ProductRestoreStockRequest productRestoreStockRequest = new ProductRestoreStockRequest(
                List.of(
                        new ProductRestoreStockInfo(1L, 1),
                        new ProductRestoreStockInfo(2L, 2)
                )
        );

        Product product1 = NormalProduct.builder()
                .id(1L)
                .price(new BigDecimal("10000"))
                .stockQuantity(10)
                .name("name1")
                .build();
        Product product2 = LimitedProduct.builder()
                .id(2L)
                .price(new BigDecimal("20000"))
                .stockQuantity(10)
                .name("name2")
                .build();

        given(productRepository.findById(1L))
                .willReturn(product1);
        given(productRepository.findById(2L))
                .willReturn(product2);

        //when
        productService.restoreStock(productRestoreStockRequest);

        //then
        assertEquals(11, product1.getStockQuantity());
        assertEquals(12, product2.getStockQuantity());
    }

    @Test
    void 존재하지_않는_특정_상품을_조회하면_예외가_발생한다() {
        //given
        given(productRepository.findById(1L))
                .willThrow(new ProductNotFoundException(1L));

        //then
        assertThrows(ProductNotFoundException.class,
                //when
                () -> productService.findById(1L)
        );

    }

    @Test
    void 특정_상품을_조회한다() {
        //given
        Product product1 = NormalProduct.builder()
                .id(1L)
                .price(new BigDecimal("10000"))
                .name("name1")
                .build();

        given(productRepository.findById(1L))
                .willReturn(product1);

        //when
        ProductDto productDto = productService.findById(1L);

        //then
        assertEquals(1L, productDto.productId());
        assertEquals("name1", productDto.name());
        assertEquals(new BigDecimal("10000"), productDto.price());
    }
}