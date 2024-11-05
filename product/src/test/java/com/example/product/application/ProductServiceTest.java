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

    @Mock
    LocalDateTimeHolder localDateTimeHolder;

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

        LocalDateTime now = LocalDateTime.of(
                2024, 11, 4, 12, 0, 0
        );
        given(localDateTimeHolder.now())
                .willReturn(now);
        given(productRepository.findAllSellableProduct(now))
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
        Object productDetails = productService.getProductDetails(1L).data();
        NormalProductDetails normalProductDetails = (NormalProductDetails) productDetails;

        //then
        assertEquals(1L, normalProductDetails.productId());
        assertEquals("name", normalProductDetails.name());
        assertEquals(new BigDecimal("10000"), normalProductDetails.price());
        assertEquals(ProductType.NORMAL.name(), normalProductDetails.type());
        assertEquals(createdAt, normalProductDetails.createdAt());
        assertEquals(updatedAt, normalProductDetails.updatedAt());
    }

    @Test
    void 한정판_상품의_상세_정보를_조회한다() {
        //given
        LocalDateTime openTime = LocalDateTime.of(
                2024, 11, 2, 12, 0, 0
        );
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
                .openTime(openTime)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        given(productRepository.findById(1L))
                .willReturn(product);

        //when
        Object productDetails = productService.getProductDetails(1L).data();
        LimitedProductDetails limitedProductDetails = (LimitedProductDetails) productDetails;


        //then
        assertEquals(1L, limitedProductDetails.productId());
        assertEquals("name", limitedProductDetails.name());
        assertEquals(new BigDecimal("10000"), limitedProductDetails.price());
        assertEquals(ProductType.LIMITED.name(), limitedProductDetails.type());
        assertEquals(createdAt, limitedProductDetails.openTime());
        assertEquals(createdAt, limitedProductDetails.createdAt());
        assertEquals(updatedAt, limitedProductDetails.updatedAt());
    }

    @Test
    void 존재하지_않는_상품을_주문하면_예외가_발생한다() {
        //given
        ProductOrderRequest productOrderRequest = new ProductOrderRequest(
                List.of(
                        new ProductOrderInfo(1L, 1)
                )
        );

        given(productRepository.findById(1L))
                .willThrow(new ProductNotFoundException(1L));

        //then
        assertThrows(ProductNotFoundException.class,
                //when
                () -> productService.getProductOrderInfo(productOrderRequest));
    }

    @Test
    void 상품_주문_시_재고가_부족하면_예외가_발생한다() {
        //given
        ProductOrderRequest productOrderRequest = new ProductOrderRequest(
                List.of(
                        new ProductOrderInfo(1L, 1)
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
                () -> productService.getProductOrderInfo(productOrderRequest));
    }

    @Test
    void 상품을_주문한다() {
        //given
        ProductOrderRequest productOrderRequest = new ProductOrderRequest(
                List.of(
                        new ProductOrderInfo(1L, 1),
                        new ProductOrderInfo(2L, 2)
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
        ProductOrderResponse response = productService.getProductOrderInfo(productOrderRequest);

        //then
        assertEquals(10, product2.getStockQuantity());
        assertEquals(10, product1.getStockQuantity());

        List<OrderedProductInfo> purchasedProductPurchaseFeignRespons = response.orderedProductInfos();
        assertEquals(2, purchasedProductPurchaseFeignRespons.size());

        OrderedProductInfo orderedProductInfo1 = purchasedProductPurchaseFeignRespons.get(0);
        assertEquals(1L, orderedProductInfo1.productId());
        assertEquals(1, orderedProductInfo1.quantity());
        assertEquals("name1", orderedProductInfo1.productName());
        assertEquals(new BigDecimal("10000"), orderedProductInfo1.amount());

        OrderedProductInfo orderedProductInfo2 = purchasedProductPurchaseFeignRespons.get(1);
        assertEquals(2L, orderedProductInfo2.productId());
        assertEquals(2, orderedProductInfo2.quantity());
        assertEquals("name2", orderedProductInfo2.productName());
        assertEquals(new BigDecimal("40000"), orderedProductInfo2.amount());
    }

    @Test
    void 존재하지_않는_상품의_재고를_복구하면_예외가_발생한다() {
        //given
        ProductRestockRequest productRestockRequest = new ProductRestockRequest(
                List.of(
                        new ProductRestockInfo(1L, 1),
                        new ProductRestockInfo(2L, 2)
                )
        );

        given(productRepository.findById(1L))
                .willThrow(new ProductNotFoundException(1L));

        //then
        assertThrows(ProductNotFoundException.class,
                // when
                () -> productService.restock(productRestockRequest));
    }

    @Test
    void 상품_재고를_복구한다() {
        //given
        ProductRestockRequest productRestockRequest = new ProductRestockRequest(
                List.of(
                        new ProductRestockInfo(1L, 1),
                        new ProductRestockInfo(2L, 2)
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
        productService.restock(productRestockRequest);

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

    @Test
    void 존재하지_않는_상품의_재고_수량_조회_시_예외가_발생한다() {
        //given
        given(productRepository.findById(1L))
                .willThrow(new ProductNotFoundException(1L));

        //then
        assertThrows(ProductNotFoundException.class,
                //when
                () -> productService.findById(1L));
    }

    @Test
    void 상품의_재고_수량을_조회한다() {
        //given
        given(productRepository.findById(1L))
                .willReturn(
                        LimitedProduct.builder()
                                .stockQuantity(3)
                                .build()
                );

        //when
        int stockQuantity = productService.getStockQuantity(1L);

        //then
        assertEquals(3, stockQuantity);
    }
}