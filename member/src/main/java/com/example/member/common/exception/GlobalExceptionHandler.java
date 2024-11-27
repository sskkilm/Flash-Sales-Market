package com.example.member.common.exception;

import com.example.member.domain.exception.MemberServiceException;
import com.example.member.domain.exception.TokenServiceException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.example.member.domain.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberServiceException.class)
    public ResponseEntity<?> handleMemberServiceException(MemberServiceException e) {
        log.info("Error Http Status: {}", e.getErrorCode().getStatus());
        log.info("Error Code: {}", e.getErrorCode().name());
        log.info("Error Code Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(TokenServiceException.class)
    public ResponseEntity<?> handleTokenServiceException(TokenServiceException e) {
        log.info("Error Http Status: {}", e.getErrorCode().getStatus());
        log.info("Error Code: {}", e.getErrorCode().name());
        log.info("Error Code Message: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(error ->
                errors.put(error.getPropertyPath().toString(), error.getMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<?> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR.name(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR.name(), e.getMessage()));
    }
}
