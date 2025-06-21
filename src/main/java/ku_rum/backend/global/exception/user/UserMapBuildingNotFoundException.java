package ku_rum.backend.global.exception.user;

import ku_rum.backend.global.support.status.ResponseStatus;
import lombok.Getter;

@Getter
public class UserMapBuildingNotFoundException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public UserMapBuildingNotFoundException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public UserMapBuildingNotFoundException(ResponseStatus exceptionStatus,String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
