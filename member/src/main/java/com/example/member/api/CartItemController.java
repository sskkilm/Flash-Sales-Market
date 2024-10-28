package com.example.member.api;

import com.example.member.application.CartItemService;
import com.example.member.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping("/cart-items/{memberId}")
    public CartItemCreateResponse create(
            @PathVariable Long memberId,
            @RequestBody @Valid CartItemCreateRequest request
    ) {
        return cartItemService.create(memberId, request);
    }

    @PatchMapping("/cart-items/{memberId}/{cartItemId}")
    public CartItemUpdateResponse update(
            @PathVariable Long memberId,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateRequest request
    ) {
        return cartItemService.update(memberId, cartItemId, request);
    }

    @DeleteMapping("/cart-items/{memberId}/{cartItemId}")
    public void delete(
            @PathVariable Long memberId,
            @PathVariable Long cartItemId
    ) {
        cartItemService.delete(memberId, cartItemId);
    }

    @GetMapping("/cart-items/{memberId}")
    public List<CartItemDto> getCartItemList(
            @PathVariable Long memberId
    ) {
        return cartItemService.getCartItemList(memberId);
    }

    @PostMapping("/cart-items/{memberId}/orders")
    public CartOrderResponse order(
            @PathVariable Long memberId
    ) {
        return cartItemService.order(memberId);
    }

}
