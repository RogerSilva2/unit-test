package br.com.capgemini.rogersilva.unittest.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.capgemini.rogersilva.unittest.dto.ErrorDto;
import br.com.capgemini.rogersilva.unittest.dto.ValidationErrorDto;
import br.com.capgemini.rogersilva.unittest.exception.BadRequestException;
import br.com.capgemini.rogersilva.unittest.exception.NotFoundException;

@ControllerAdvice
public class ExceptionHandlerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> badRequest(BadRequestException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(ErrorDto.builder().error("bad_request").errorDescription(e.getMessage()).build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> badRequest(HttpMessageNotReadableException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorDto.builder().error("missing_required_body")
                .errorDescription(e.getMessage().split(":")[0]).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> badRequest(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage(), e);
        List<ValidationErrorDto> validations = e.getBindingResult().getFieldErrors().stream().filter(Objects::nonNull)
                .map(f -> new ValidationErrorDto(f.getObjectName(), f.getField(), f.getRejectedValue(),
                        f.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(
                new ErrorDto("invalid_fields", "Validation errors were found in the submitted fields", validations));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> badRequest(MethodArgumentTypeMismatchException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorDto.builder().error("type_mismatch")
                .errorDescription(String.format("%s with value %s not is valid", e.getName(), e.getValue())).build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> unauthorized(BadCredentialsException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorDto.builder().error("unauthorized").errorDescription(e.getMessage()).build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> forbidden(AccessDeniedException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDto.builder().error("access_denied").errorDescription(e.getMessage()).build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> notFound(NotFoundException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorDto.builder().error("not_found").errorDescription(e.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> internalServerError(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDto.builder().error("internal_server_error").errorDescription(e.getMessage()).build());
    }
}