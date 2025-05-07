package ku_rum.backend.global.exception.department;

import ku_rum.backend.global.support.status.ResponseStatus;
import lombok.Getter;

@Getter
public class DuplicateDepartmentException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public DuplicateDepartmentException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public DuplicateDepartmentException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }

}
