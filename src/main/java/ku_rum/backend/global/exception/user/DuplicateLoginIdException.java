package ku_rum.backend.global.exception.user;

import ku_rum.backend.global.support.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class DuplicateLoginIdException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public DuplicateLoginIdException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public DuplicateLoginIdException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }

}
