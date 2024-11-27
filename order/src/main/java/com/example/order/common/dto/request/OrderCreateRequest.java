package com.example.order.common.dto.request;

import com.example.order.common.dto.OrderInfo;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequest(
        @NotEmpty List<OrderInfo> orderInfos
) {
}
