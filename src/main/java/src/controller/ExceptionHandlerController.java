package src.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import src.model.exceptions.GenericReaderException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({GenericReaderException.class})
    public ResponseEntity<String> handleGenericException(GenericReaderException exception) {
        return error(HttpStatus.BAD_REQUEST, exception);
    }

    private ResponseEntity<String> error(HttpStatus status, Exception exception) {
        return ResponseEntity.status(status).body(exception.getMessage());
    }
}
