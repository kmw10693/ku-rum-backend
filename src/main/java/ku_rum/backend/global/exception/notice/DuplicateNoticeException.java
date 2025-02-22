package ku_rum.backend.global.exception.notice;

import ku_rum.backend.global.support.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class DuplicateNoticeException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public DuplicateNoticeException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public DuplicateNoticeException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }

}
