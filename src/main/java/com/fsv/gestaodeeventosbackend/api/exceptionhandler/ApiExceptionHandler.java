package com.fsv.gestaodeeventosbackend.api.exceptionhandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldValidationError> fieldErrors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> FieldValidationError.builder()
                        .name(error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .title("Um ou mais campos estão inválidos.")
                .fields(fieldErrors)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Problem problem = Problem.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .title("Corpo da requisição está mal formatado ou contém dados inválidos.")
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {

        Problem problem = Problem.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(OffsetDateTime.now())
                .title(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {

        Problem problem = Problem.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(OffsetDateTime.now())
                .title("Recurso não encontrado: " + ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, HttpServletRequest request) {
        Problem problem = Problem.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(OffsetDateTime.now())
                .title("Erro interno no servidor. Contate o administrador.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
