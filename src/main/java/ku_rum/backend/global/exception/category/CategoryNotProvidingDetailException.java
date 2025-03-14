package ku_rum.backend.global.exception.category;

import ku_rum.backend.global.support.status.BaseExceptionResponseStatus;
import lombok.Getter;

@Getter
public class CategoryNotProvidingDetailException extends RuntimeException {
    private BaseExceptionResponseStatus status;

    public CategoryNotProvidingDetailException(BaseExceptionResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}