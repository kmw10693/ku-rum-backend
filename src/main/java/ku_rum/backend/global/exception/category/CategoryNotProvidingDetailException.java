package ku_rum.backend.global.exception.category;

import ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus;
import lombok.Getter;

@Getter
public class CategoryNotProvidingDetailException extends RuntimeException {
  private BaseExceptionResponseStatus status;
  public CategoryNotProvidingDetailException(BaseExceptionResponseStatus status) {
    this.status = status;
  }
}