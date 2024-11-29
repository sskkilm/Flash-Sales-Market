package com.port90.cartitem.application;

import com.port90.cartitem.application.port.feign.OrderClient;
import com.port90.cartitem.application.port.feign.ProductClient;
import com.port90.cartitem.application.port.CartItemRepository;
import com.port90.cartitem.common.dto.CartItemDto;
import com.port90.cartitem.common.dto.ProductDto;
import com.port90.cartitem.common.dto.request.CartItemCreateRequest;
import com.port90.cartitem.common.dto.OrderInfo;
import com.port90.cartitem.common.dto.request.CartItemUpdateRequest;
import com.port90.cartitem.common.dto.request.OrderCreateRequest;
import com.port90.cartitem.common.dto.response.CartItemCreateResponse;
import com.port90.cartitem.common.dto.response.CartItemUpdateResponse;
import com.port90.cartitem.common.dto.response.OrderCreateResponse;
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
    private final ProductClient productClient;
    private final OrderClient orderClient;

    public CartItemCreateResponse create(Long memberId, CartItemCreateRequest request) {
        ProductDto productDto = productClient.getProductInfo(request.productId());
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
                    ProductDto productDto = productClient.getProductInfo(cartItem.getProductId());

                    return new CartItemDto(cartItem.getId(), productDto.name(), cartItem.getQuantity());
                }
        ).toList();
    }

    public OrderCreateResponse order(Long memberId) {
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(memberId);
        if (cartItems.isEmpty()) {
            throw new CartItemException(CART_IS_EMPTY);
        }

        OrderCreateRequest request = new OrderCreateRequest(
                cartItems
                        .stream()
                        .map(cartItem -> new OrderInfo(
                                cartItem.getProductId(), cartItem.getQuantity()
                        )).toList()
        );
        OrderCreateResponse orderCreateResponse = orderClient.create(memberId, request);

        cartItemRepository.deleteAll(cartItems);

        return orderCreateResponse;
    }
}
