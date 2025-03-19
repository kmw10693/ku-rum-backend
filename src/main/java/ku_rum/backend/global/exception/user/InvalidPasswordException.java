package ku_rum.backend.global.exception.user;

import ku_rum.backend.global.support.status.ResponseStatus;
import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public InvalidPasswordException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public InvalidPasswordException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }

}