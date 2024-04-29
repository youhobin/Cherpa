package com.cerpha.productservice.common.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponseDto<T> {

    private int number;
    private int size;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
    private List<T> data;

    public PageResponseDto(int number, int size, int totalPages, boolean isFirst, boolean isLast, List<T> data) {
        this.number = number;
        this.size = size;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.data = data;
    }
}
