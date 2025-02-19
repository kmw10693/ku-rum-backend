package ku_rum.backend.global.exception.category;

import ku_rum.backend.global.response.status.BaseExceptionResponseStatus;
import lombok.Getter;

@Getter
public class CategoryNotExistException extends RuntimeException {
  private BaseExceptionResponseStatus status;
  public CategoryNotExistException(BaseExceptionResponseStatus status) {
    this.status = status;
  }
}
