package ku_rum.backend.global.exception.user;

import ku_rum.backend.global.support.status.ResponseStatus;

public class NoSuchLoginIdException extends RuntimeException {
    private final ResponseStatus status;

    public NoSuchLoginIdException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.status = responseStatus;
    }
}
