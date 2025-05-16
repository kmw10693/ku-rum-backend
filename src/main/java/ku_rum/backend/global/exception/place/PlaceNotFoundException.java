package ku_rum.backend.global.exception.place;

import ku_rum.backend.global.support.status.ResponseStatus;
import lombok.Getter;

@Getter
public class PlaceNotFoundException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public PlaceNotFoundException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public PlaceNotFoundException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}