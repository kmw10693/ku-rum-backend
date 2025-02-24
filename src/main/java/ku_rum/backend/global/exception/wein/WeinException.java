package ku_rum.backend.global.exception.wein;

import ku_rum.backend.global.support.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class WeinException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public WeinException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public WeinException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }

}
