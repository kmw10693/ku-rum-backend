package ku_rum.backend.global.exception.global;

import ku_rum.backend.global.support.status.BaseExceptionResponseStatus;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final BaseExceptionResponseStatus status;

    public GlobalException(BaseExceptionResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }

}
