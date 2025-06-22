package ku_rum.backend.global.exception.place;

import ku_rum.backend.global.support.status.ResponseStatus;
import lombok.Getter;

@Getter
public class PlaceImageNotFoundException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public PlaceImageNotFoundException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public PlaceImageNotFoundException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
