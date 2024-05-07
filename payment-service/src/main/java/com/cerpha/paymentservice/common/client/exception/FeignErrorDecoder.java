package com.cerpha.paymentservice.common.client.exception;

import com.cerpha.paymentservice.common.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public FeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String s, Response response) {
        if (response.body() != null) {
            try {
                ExceptionResponse exceptionResponse = objectMapper.readValue(response.body().asInputStream(), ExceptionResponse.class);
                return new FeignClientException(exceptionResponse);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
