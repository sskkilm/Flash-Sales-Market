package com.example.member.application;

import com.example.member.application.feign.OrderFeignClient;
import com.example.member.application.feign.ProductFeignClient;
import com.example.member.domain.CartItem;
import com.example.member.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductFeignClient productFeignClient;
    private final OrderFeignClient orderFeignClient;

    public CartItemCreateResponse create(Long memberId, CartItemCreateRequest request) {
        ProductDto productDto = productFeignClient.findById(request.productId());
        CartItem cartItem = cartItemRepository.save(
                CartItem.create(memberId, productDto.productId(), request.quantity())
        );

        return CartItemCreateResponse.from(cartItem);
    }

    public CartItemUpdateResponse update(Long memberId, Long cartItemId, CartItemUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId);
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
        CartItem cartItem = cartItemRepository.findById(cartItemId);
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
                    ProductDto productDto = productFeignClient.findById(cartItem.getProductId());

                    return new CartItemDto(cartItem.getId(), productDto.name(), cartItem.getQuantity());
                }
        ).toList();
    }

    public CartOrderResponse order(Long memberId) {
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(memberId);

        List<CartItemOrderRequest> cartItemOrderRequests = cartItems.stream().map(cartItem ->
                new CartItemOrderRequest(cartItem.getProductId(), cartItem.getQuantity())
        ).toList();

        CartOrderResponse cartOrderResponse = orderFeignClient.create(
                memberId, new CartOrderRequest(cartItemOrderRequests)
        );

        cartItemRepository.deleteAll(cartItems);

        return cartOrderResponse;
    }
}
