package com.login.login_app.exception;

import com.login.login_app.exception.exceptions.ExpiredException;
import com.login.login_app.exception.exceptions.HasConfirmed;
import com.login.login_app.exception.exceptions.NotFoundException;
import com.login.login_app.exception.exceptions.ValidEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.ZonedDateTime;


@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleExceptions(ApiRequestException exception) {
        var apiException = new ApiException(exception.getMessage(), exception, HttpStatus.BAD_REQUEST, ZonedDateTime.now());

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = ValidEmailException.class)
    public ResponseEntity<Object> handleExceptions(ValidEmailException exception) {
        var apiException = new ApiException(exception.getMessage(), exception, HttpStatus.NOT_ACCEPTABLE, ZonedDateTime.now());

        return new ResponseEntity<>(apiException, HttpStatus.NOT_ACCEPTABLE);

    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleExceptions(NotFoundException exception) {
        var apiException = new ApiException(exception.getMessage(), exception, HttpStatus.NOT_FOUND, ZonedDateTime.now());

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = HasConfirmed.class)
    public ResponseEntity<Object> handleExceptions(HasConfirmed exception) {
        var apiException = new ApiException(exception.getMessage(), exception, HttpStatus.METHOD_NOT_ALLOWED, ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(value = ExpiredException.class)
    public ResponseEntity<Object> handleExceptions(ExpiredException exception) {
        var apiException = new ApiException(exception.getMessage(), exception, HttpStatus.REQUEST_TIMEOUT, ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.REQUEST_TIMEOUT);
    }


}
