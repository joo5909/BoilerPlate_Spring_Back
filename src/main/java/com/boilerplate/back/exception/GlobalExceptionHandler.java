package com.boilerplate.back.exception;

import com.boilerplate.back.common.ResponseCode;
import com.boilerplate.back.dto.response.ResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessages = new StringBuilder();

        // 검증된 필드 에러 처리
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessages.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(" ");
        }

        // Validation 실패시 적절한 ResponseDto 반환
        ResponseDto responseBody = new ResponseDto(ResponseCode.VALIDATION_FAILED, errorMessages.toString());

        // 400 Bad Request 응답과 함께 반환
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // 추가적인 예외 핸들러를 작성할 수 있습니다.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGeneralException(Exception ex) {
        ResponseDto responseBody = new ResponseDto(ResponseCode.INTERNAL_ERROR, "Internal server error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
