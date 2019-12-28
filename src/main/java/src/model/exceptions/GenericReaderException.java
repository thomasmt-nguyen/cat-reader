package src.model.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import src.enums.ErrorCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class GenericReaderException extends RuntimeException {
    private String message;
    private ErrorCode errorCode;
}
