package com.shop.productservice.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> {
    private int status;
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private PageMeta page;

    public static <T> ApiResponse<T> success(String message){
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status){
        return ApiResponse.<T>builder()
                .success(true)
                .status(status.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<List<T>> page(Page<T> data, String message){
        String finalMessage = data.isEmpty()
                ? "Không có dữ liệu"
                : message;
        return ApiResponse.<List<T>>builder()
                .success(!data.isEmpty())
                .message(finalMessage)
                .data(data.getContent())
                .page(PageMeta.from(data))
                .build();
    }
}
