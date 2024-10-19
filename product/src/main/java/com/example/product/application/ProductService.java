package com.example.product.application;

import com.example.product.domain.Product;
import com.example.product.dto.ProductDetails;
import com.example.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDto> getProductList() {
        return productRepository.findAll()
                .stream().map(ProductDto::of).toList();
    }

    public ProductDetails getProductDetails(Long id) {
        Product product = productRepository.findById(id);
        return ProductDetails.toDto(product);
    }
}
