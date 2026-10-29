package com.shop.productservice.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageMeta(
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    public static PageMeta from(Page<?> page) {
        return PageMeta.builder()
                .number(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}