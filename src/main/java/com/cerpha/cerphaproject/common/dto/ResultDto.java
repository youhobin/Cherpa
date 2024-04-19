package com.cerpha.cerphaproject.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ResultDto<T> {

    private HttpStatus status;
    private T resultData;

    public ResultDto(HttpStatus status) {
        this(status, null);
    }

    public ResultDto(HttpStatus status, T resultData) {
        this.status = status;
        this.resultData = resultData;
    }
}
