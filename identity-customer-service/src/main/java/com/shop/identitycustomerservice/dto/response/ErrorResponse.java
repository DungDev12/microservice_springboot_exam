package com.shop.identitycustomerservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private T errors;
    private LocalDateTime timestamp;

    public static <T> ErrorResponse<?> error( String message, HttpStatus status) {
        return ErrorResponse.builder()
                .success(false)
                .statusCode(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ErrorResponse<?> errorValidation(T data) {
        return ErrorResponse.builder()
                .success(false)
                .statusCode(400)
                .message("Dữ liệu đầu vào không hợp lệ")
                .errors(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
