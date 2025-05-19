package com.example.schedulejpaapi.exceptions;

import com.example.schedulejpaapi.exceptions.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Request 검증 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    // 수정 불가능한 필드 접근 예외 핸들러
    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<String> handleInvalidFieldException(InvalidFieldException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    // 이미 가입한 아이디 검증 핸들러
    @ExceptionHandler(AlreadyAccountException.class)
    public ResponseEntity<String> handleAlreadyAccountException(AlreadyAccountException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(message);
    }

    // 인증 실패 핸들러
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    // 비밀번호 불일치 핸들러
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handleIncorrectPasswordException(IncorrectPasswordException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    // 존재하지 않는 게시물 핸들러
    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<String> handleNotFoundPostException(NotFoundPostException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
