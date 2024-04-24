package com.cerpha.cerphaproject.cerpha.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AddOrderRequest {

    @NotNull(message = "유저 ID가 필요합니다.")
    private Long userId;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    @Size(max = 100, message = "주소는 100자 이하이어야 합니다.")
    private String deliveryAddress;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Size(max = 20, message = "전화번호는 20자 이하이어야 합니다.")
    private String deliveryPhone;

    @Valid
    List<AddOrderProductRequest> orderProducts;

    @Builder
    public AddOrderRequest(Long userId, String deliveryAddress, String deliveryPhone, List<AddOrderProductRequest> orderProducts) {
        this.userId = userId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.orderProducts = orderProducts;
    }
}
