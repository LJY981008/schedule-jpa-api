package com.example.schedulejpaapi.exceptions;

import com.example.schedulejpaapi.exceptions.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * API 전역에서 발생하는 예외처리 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * {@link MethodArgumentNotValidException} 발생 시 호출
     * RequestBody의 유효성 검증 실패 시 호출
     *
     * @param ex 발생한 예외 객체
     * @return 에러메세지와 상태 코드 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    /**
     * {@link InvalidFieldException} 발생 시 호출
     * 접근 불가능한 필드 접근시 호출
     *
     * @param ex 발생한 예외 객체
     * @return 에러메세지와 상태 코드 반환
     */
    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<String> handleInvalidFieldException(InvalidFieldException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    /**
     * {@link AlreadyAccountException} 발생 시 호출
     * 이미 로그인된 상태에서 로그인 시도 시 호출
     *
     * @param ex 발생한 예외 객체
     * @return 에러메세지와 상태 코드 반환
     */
    @ExceptionHandler(AlreadyAccountException.class)
    public ResponseEntity<String> handleAlreadyAccountException(AlreadyAccountException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(message);
    }

    /**
     * {@link UnauthorizedException} 발생 시 호출
     * 로그인 인증 실패 시 호출
     *
     * @param ex 발생한 예외 객체
     * @return 에러메세지와 상태 코드 반환
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    /**
     * {@link IncorrectPasswordException} 발생 시 호출
     * 비밀번호가 틀렸을 때 호출
     *
     * @param ex 발생한 예외 객체
     * @return 에러메세지와 상태 코드 반환
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handleIncorrectPasswordException(IncorrectPasswordException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    /**
     * {@link NotFoundPostException} 발생 시 호출
     * 탐색한 데이터가 존재하지 않을 시 호출
     *
     * @param ex 발생한 예외 객체
     * @return 에러메세지와 상태 코드 반환
     */
    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<String> handleNotFoundPostException(NotFoundPostException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
