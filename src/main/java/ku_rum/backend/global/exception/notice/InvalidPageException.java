package ku_rum.backend.global.exception.notice;

import ku_rum.backend.global.support.status.ResponseStatus;
import lombok.Getter;

@Getter
public class InvalidPageException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public InvalidPageException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public InvalidPageException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }

}
