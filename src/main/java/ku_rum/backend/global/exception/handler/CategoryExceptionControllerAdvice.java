package ku_rum.backend.global.exception.handler;

import jakarta.annotation.Priority;
import ku_rum.backend.global.exception.category.CategoryNotExistException;
import ku_rum.backend.global.exception.category.CategoryNotProvidingDetailException;
import ku_rum.backend.global.support.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus.CATEGORYNAME_NOT_PROVIDING_DETAIL;
import static ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus.CATEGORY_NAME_NOT_EXIST;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class CategoryExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryNotExistException.class)
    public BaseErrorResponse handleCategoryNotExistException(final CategoryNotExistException e){
        log.error("[handle_CategoryException]");
        return new BaseErrorResponse(CATEGORY_NAME_NOT_EXIST);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(CategoryNotProvidingDetailException.class)
    public BaseErrorResponse handleCategoryNotProvidingDetail(final CategoryNotProvidingDetailException e){
        log.error("[handle_CategoryException]");
        return new BaseErrorResponse(CATEGORYNAME_NOT_PROVIDING_DETAIL);
    }
}
