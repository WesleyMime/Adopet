package com.adopet;

import com.adopet.exceptions.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ProblemDetail> authorizationDeniedExceptionHandler(AuthorizationDeniedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setDetail("You don't have authorization to access this endpoint.");
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> badCredentialsExceptionHandler(BadCredentialsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setDetail("Invalid e-mail and password.");
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    // Invalid abrigo UUID
    @ExceptionHandler(InvalidAbrigoException.class)
    public ResponseEntity<ProblemDetail> invalidAbrigoExceptionHandler(InvalidAbrigoException e) {
        e.setTitle("Abrigo not found");
        e.setDetail("There is no abrigo with this id.");
        return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
    }

    // Invalid pet UUID
    @ExceptionHandler(InvalidPetException.class)
    public ResponseEntity<ProblemDetail> invalidPetExceptionHandler(InvalidPetException e) {
        e.setTitle("Pet not found");
        e.setDetail("There is no pet with this id.");
        return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
    }

    // Pet already adopted
    @ExceptionHandler(PetAlreadyAdoptedException.class)
    public ResponseEntity<ProblemDetail> petAlreadyAdoptedExceptionHandler(PetAlreadyAdoptedException e) {
        e.setTitle("Pet already adopted");
        e.setDetail("This pet was already adopted.");
        return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
    }

    // Invalid tutor UUID
    @ExceptionHandler(InvalidTutorException.class)
    public ResponseEntity<ProblemDetail> invalidTutorExceptionHandler(InvalidTutorException e) {
        e.setTitle("Tutor not found");
        e.setDetail("There is no tutor with this id.");
        return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
    }

    // Email already registered
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException e) {
        e.setTitle("Email already registered");
        e.setDetail("You tried to use an email that already is in use.");
        return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
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
