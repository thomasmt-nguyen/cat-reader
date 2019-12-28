package src.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import src.enums.ErrorCode;
import src.model.exceptions.GenericReaderException;

@ControllerAdvice
public class ExceptionHandlerController {

    private Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler({GenericReaderException.class})
    public ResponseEntity<String> handleGenericException(GenericReaderException exception) {

        ErrorCode errorCode = exception.getErrorCode();
        HttpStatus httpStatus;
        switch(errorCode) {
            case INVALID_IMAGE_SIZE:
            case INVALID_IMAGE_TYPE:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            case READ_FILE_ERROR:
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        logger.error("Error: errorCode={}, errorMessage={}", errorCode, exception.getMessage());

        return error(httpStatus, exception);
    }

    private ResponseEntity<String> error(HttpStatus status, Exception exception) {
        return ResponseEntity.status(status).body(exception.getMessage());
    }
}
