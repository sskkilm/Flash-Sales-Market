package com.example.member.application;

import com.example.member.domain.CartItem;
import com.example.member.dto.CartItemCreateRequest;
import com.example.member.dto.CartItemUpdateRequest;
import com.example.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public void create(Long memberId, CartItemCreateRequest request) {
        Long checkedProductId = productService.findById(request.productId());
        CartItem cartItem = CartItem.create(memberId, checkedProductId, request.quantity());
        cartItemRepository.save(cartItem);
    }

    public void update(Long memberId, CartItemUpdateRequest request) {
        Long checkedProductId = productService.findById(request.productId());
        CartItem cartItem = cartItemRepository.findByMemberIdAndProductId(memberId, checkedProductId);
        cartItem.updateQuantity(request.quantity());
        cartItemRepository.save(cartItem);
    }
}
