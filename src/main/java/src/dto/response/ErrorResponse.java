package src.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import src.enums.ErrorCode;

@Data
@Builder
public class ErrorResponse {
    private HttpStatus httpStatus;
    private ErrorCode errorCode;
    private String message;
}
