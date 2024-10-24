package com.example.member.application;

import com.example.member.domain.CartItem;
import com.example.member.dto.*;
import com.example.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItemCreateResponse create(Long memberId, CartItemCreateRequest request) {
        Long checkedProductId = productService.findById(request.productId());
        CartItem cartItem = cartItemRepository.save(
                CartItem.create(memberId, checkedProductId, request.quantity())
        );

        return CartItemCreateResponse.from(cartItem);
    }

    public CartItemUpdateResponse update(Long memberId, Long cartItemId, CartItemUpdateRequest request) {
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
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        return CartItemUpdateResponse.from(updatedCartItem);
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

    public List<CartItemDto> getCartItemList(Long memberId) {
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(memberId);

        return cartItems.stream().map(
                cartItem -> {
                    String productName = productService.findProductNameByProductId(cartItem.getProductId());

                    return new CartItemDto(cartItem.getId(), productName, cartItem.getQuantity());
                }
        ).toList();
    }
}
