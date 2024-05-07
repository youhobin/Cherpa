package com.cerpha.paymentservice.common.client.product;

import com.cerpha.paymentservice.common.client.product.request.RestoreStockRequest;
import com.cerpha.paymentservice.common.dto.ResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {

    @PostMapping("/api/internal/products/cancel")
    ResultDto restoreStock(@RequestBody RestoreStockRequest restoreStockRequest);
}
