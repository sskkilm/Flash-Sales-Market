package com.port90.cartitem.application;

import com.port90.cartitem.application.feign.OrderFeignClient;
import com.port90.cartitem.application.feign.ProductFeignClient;
import com.port90.cartitem.application.port.CartItemRepository;
import com.port90.cartitem.common.dto.CartItemDto;
import com.port90.cartitem.common.dto.ProductDto;
import com.port90.cartitem.common.dto.request.CartItemCreateRequest;
import com.port90.cartitem.common.dto.request.CartItemOrderRequest;
import com.port90.cartitem.common.dto.request.CartItemUpdateRequest;
import com.port90.cartitem.common.dto.response.CartItemCreateResponse;
import com.port90.cartitem.common.dto.response.CartItemUpdateResponse;
import com.port90.cartitem.common.dto.response.CartOrderResponse;
import com.port90.cartitem.domain.exception.CartItemException;
import com.port90.cartitem.domain.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.port90.cartitem.domain.exception.ErrorCode.CART_IS_EMPTY;
import static com.port90.cartitem.domain.exception.ErrorCode.MEMBER_INFO_UN_MATCHED;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductFeignClient productFeignClient;
    private final OrderFeignClient orderFeignClient;

    public CartItemCreateResponse create(Long memberId, CartItemCreateRequest request) {
        ProductDto productDto = productFeignClient.getProductInfo(request.productId());
        CartItem cartItem = cartItemRepository.save(
                CartItem.create(memberId, productDto.productId(), request.quantity())
        );

        return CartItemCreateResponse.from(cartItem);
    }

    public CartItemUpdateResponse update(Long memberId, Long cartItemId, CartItemUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isNotIncludedBy(memberId)) {
            throw new CartItemException(MEMBER_INFO_UN_MATCHED);
        }
        cartItem.updateQuantity(request.quantity());
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        return CartItemUpdateResponse.from(updatedCartItem);
    }

    public void delete(Long memberId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isNotIncludedBy(memberId)) {
            throw new CartItemException(MEMBER_INFO_UN_MATCHED);
        }

        cartItemRepository.delete(cartItem);
    }

    public List<CartItemDto> getCartItemList(Long memberId) {
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(memberId);

        return cartItems.stream().map(
                cartItem -> {
                    ProductDto productDto = productFeignClient.getProductInfo(cartItem.getProductId());

                    return new CartItemDto(cartItem.getId(), productDto.name(), cartItem.getQuantity());
                }
        ).toList();
    }

    public CartOrderResponse order(Long memberId) {
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(memberId);
        if (cartItems.isEmpty()) {
            throw new CartItemException(CART_IS_EMPTY);
        }

        List<CartItemOrderRequest> cartItemOrderRequests = cartItems
                .stream()
                .map(cartItem -> new CartItemOrderRequest(
                                cartItem.getProductId(), cartItem.getQuantity()
                        )
                ).toList();

        CartOrderResponse cartOrderResponse = orderFeignClient.cartOrder(
                memberId, cartItemOrderRequests
        );

        cartItemRepository.deleteAll(cartItems);

        return cartOrderResponse;
    }
}
