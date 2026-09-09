package com.example.blogapi.exception;


import com.example.blogapi.dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<Void> handleNotFound(
            ResourceNotFoundException ex,
            Locale locale){
        String message = messageSource.getMessage(ex.getMessage(), null, locale);

        return BaseResponse.error(message);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse<Void> handleForbidden(
            ForbiddenException ex,
            Locale locale){
        String message = messageSource.getMessage(ex.getMessage(), null, locale);

        return BaseResponse.error(message);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<Void> handleInvalidCredentials(
            InvalidCredentialsException ex,
            Locale locale
    ) {
        String message = messageSource.getMessage(ex.getMessage(), null, locale);

        return BaseResponse.error(message);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public BaseResponse<Void> handleConflictException(
            ConflictException ex,
            Locale locale){
        String message = messageSource.getMessage(ex.getMessage(), null, locale);

        return BaseResponse.error(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<Void> handleValidation(
            MethodArgumentNotValidException ex,
            Locale locale
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> messageSource.getMessage(
                        error.getDefaultMessage(),
                        null,
                        locale
                ))
                .findFirst()
                .orElseGet(() -> messageSource.getMessage("validation.failed", null, locale));

        return BaseResponse.error(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<Void> handleException(
            Exception ex,
            Locale locale) {
        log.error("Unexpected error", ex);

        String message = messageSource.getMessage(
                "internal.server.error",
                null,
                locale
        );

        return BaseResponse.error(message);
    }

}
