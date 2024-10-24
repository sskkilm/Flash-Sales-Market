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

    public void update(Long memberId, Long cartItemId, CartItemUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "cart item not fount -> cartItemId: " + cartItemId
                ));
        if (cartItem.isNotIncludedBy(memberId)) {
            throw new IllegalArgumentException(
                    "this cart item is not included by this member -> memberId: " + memberId
            );
        }
        cartItem.updateQuantity(request.quantity());
        cartItemRepository.save(cartItem);
    }

    public void delete(Long memberId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "cart item not fount -> cartItemId: " + cartItemId
                ));
        if (cartItem.isNotIncludedBy(memberId)) {
            throw new IllegalArgumentException(
                    "this cart item is not included by this member -> memberId: " + memberId
            );
        }

        cartItemRepository.delete(cartItem);
    }
}
