package com.cerpha.orderservice.cerpha.order.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddOrderProductRequest {

    @NotNull(message = "상품 번호가 필요합니다.")
    @Min(value = 1, message = "상품 번호가 1보다 작을 수 없습니다.")
    private Long productId;

    @NotNull(message = "수량이 필요합니다.")
    @Min(value = 1, message = "수량은 1보다 작을 수 없습니다.")
    private Long unitCount;

}
