package src.model.exceptions;

import lombok.Builder;
import lombok.Data;
import src.enums.ErrorCode;

@Data
@Builder
public class GenericReaderException extends RuntimeException {
    private String message;
    private ErrorCode errorCode;
}
