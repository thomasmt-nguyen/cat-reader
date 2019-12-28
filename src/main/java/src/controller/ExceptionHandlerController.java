package src.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import src.dto.response.ErrorResponse;
import src.enums.ErrorCode;
import src.model.exceptions.GenericReaderException;

@ControllerAdvice
public class ExceptionHandlerController {

    private Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        logger.error("Error: exception={}", exception.getMessage());
        return error(errorResponse);
    }


    @ExceptionHandler({GenericReaderException.class})
    public ResponseEntity<ErrorResponse> handleGenericException(GenericReaderException exception) {

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

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode(errorCode)
                .httpStatus(httpStatus)
                .build();

        logger.error("Error: errorCode={}, errorMessage={}", errorCode, exception.getMessage());

        return error(errorResponse);
    }

    private ResponseEntity<ErrorResponse> error(ErrorResponse errorResponse) {
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }
}
