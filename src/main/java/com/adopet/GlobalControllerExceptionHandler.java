package com.adopet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    // Unique index or primary key violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Email already registered.");
        problemDetail.setDetail("You tried to use an email that already is in use.");
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    // Parameter type different from expected. e.g. (String instead of UUID)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Method parameter '" + e.getPropertyName() + "' is invalid.");
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    // Form with invalid/missing values
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = e.getBody();
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        List<FieldErrorDetail> errors = new ArrayList<>();
        e.getFieldErrors().forEach(fieldError ->
                errors.add(new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage())));
        problemDetail.setProperty("errors", errors);
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    // Post/Put/Patch without form
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Invalid request content.");
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    // Handler catch all
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> runtimeExceptionHandler(Exception e) {
        logger.error(String.valueOf(e));
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("Something went wrong.");
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @AllArgsConstructor
    @Getter
    private static class FieldErrorDetail {

        private String field;

        private String detail;

    }
}
