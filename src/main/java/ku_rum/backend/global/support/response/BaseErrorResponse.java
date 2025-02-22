package ku_rum.backend.global.support.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ku_rum.backend.global.support.response.status.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"code", "status", "message"})
public class BaseErrorResponse implements ResponseStatus {
    private final int code;
    private final HttpStatus status;
    private final String message;

    public BaseErrorResponse(HttpStatus status, String message) {
        this.code = status.value();
        this.status = status;
        this.message = message;
    }

    public BaseErrorResponse(ResponseStatus status) {
        this.code = status.getCode();
        this.status = status.getStatus();
        this.message = status.getMessage();
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
