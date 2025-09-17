package com.example.study.exception;

import com.example.study.Entity.AppException;
import com.example.study.Entity.ErrorCode;
import com.example.study.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandle {
  @ExceptionHandler(value = RuntimeException.class)
  ResponseEntity<String> runtimeExceptionHandle(RuntimeException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  @ExceptionHandler(value = AppException.class)
  ResponseEntity<ApiResponse> runtimeExceptionHandle(AppException e) {
    ErrorCode errorCode = e.getError();
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());
    return ResponseEntity.badRequest().body(apiResponse);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  ResponseEntity<String> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
    return ResponseEntity.badRequest().body(e.getFieldError().getDefaultMessage());
  }
}
