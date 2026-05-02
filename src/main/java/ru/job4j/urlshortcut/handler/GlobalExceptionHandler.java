package ru.job4j.urlshortcut.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.job4j.urlshortcut.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 1. Нарушение уникальности (регистрация дубликата) */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation() {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("SITE_ALREADY_EXISTS",
                        "Сайт уже зарегистрирован"));
    }

    /* 2. Неверный логин/пароль */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("INVALID_CREDENTIALS",
                        "Неверный логин или пароль"));
    }

    /* 3. Короткая ссылка не найдена */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
        if (e.getMessage().equals("URL not found")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("URL_NOT_FOUND",
                            "Ссылка не найдена"));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR",
                        "Внутренняя ошибка сервера"));
    }
}