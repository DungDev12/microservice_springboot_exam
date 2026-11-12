package com.shop.customerservice.exception;

import com.shop.customerservice.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<Map<String,String>> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String > error = new HashMap<>();
                    error.put("field", fieldError.getField());
                    error.put("message",fieldError.getDefaultMessage());
                    return error;
                }).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.errorValidation(errors, "Dữ liệu không hợp lệ"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse<?>> handleAccessDeniedException(AccessDeniedException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.error(e.getMessage(),e.getHttpStatus()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse<?>> handleNotFoundException(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.error(e.getMessage(),HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<ErrorResponse<?>> handleAccountNotVerifiedException(AccountNotVerifiedException e){
        Map<String, Object> errors = new HashMap<>();
        errors.put("code", "EMAIL_NOT_VERIFIED");
        errors.put("nextStep", "VERIFY_EMAIL");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.errorValidation(errors,e.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse<?>> handleAlreadyExists(AlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.error(e.getMessage(),HttpStatus.CONFLICT));
    }

    @ExceptionHandler(InternalServerException.class)
    public  ResponseEntity<ErrorResponse<?>> handleInternalServerException(InternalServerException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.error(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
