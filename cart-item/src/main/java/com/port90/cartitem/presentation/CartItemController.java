package com.port90.cartitem.presentation;

import com.port90.cartitem.application.CartItemService;
import com.port90.cartitem.common.dto.*;
import com.port90.cartitem.common.dto.request.CartItemCreateRequest;
import com.port90.cartitem.common.dto.request.CartItemUpdateRequest;
import com.port90.cartitem.common.dto.response.CartItemCreateResponse;
import com.port90.cartitem.common.dto.response.CartItemUpdateResponse;
import com.port90.cartitem.common.dto.response.CartOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart-items")
public class CartItemController {

    private static final String X_MEMBER_ID = "X-Member-Id";
    private final CartItemService cartItemService;

    @PostMapping
    public CartItemCreateResponse create(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @RequestBody @Valid CartItemCreateRequest request
    ) {
        return cartItemService.create(memberId, request);
    }

    @PatchMapping("/{cartItemId}")
    public CartItemUpdateResponse update(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateRequest request
    ) {
        return cartItemService.update(memberId, cartItemId, request);
    }

    @DeleteMapping("/{cartItemId}")
    public void delete(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @PathVariable Long cartItemId
    ) {
        cartItemService.delete(memberId, cartItemId);
    }

    @GetMapping
    public List<CartItemDto> getCartItemList(
            @RequestHeader(X_MEMBER_ID) Long memberId
    ) {
        return cartItemService.getCartItemList(memberId);
    }

    @PostMapping("/orders")
    public CartOrderResponse order(
            @RequestHeader(X_MEMBER_ID) Long memberId
    ) {
        return cartItemService.order(memberId);
    }

}
