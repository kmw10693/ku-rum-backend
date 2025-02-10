package ku_rum.backend.global.handler;

import jakarta.annotation.Priority;
import ku_rum.backend.global.exception.category.CategoryNotExist;
import ku_rum.backend.global.exception.category.CategoryNotProvidingDetail;
import ku_rum.backend.global.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.CATEGORYNAME_NOT_PROVIDING_DETAIL;
import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.CATEGORY_NAME_NOT_EXIST;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class CategoryExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryNotExist.class)
    public BaseErrorResponse handleCategoryNotExistException(final CategoryNotExist e){
        log.error("[handle_CategoryException]");
        return new BaseErrorResponse(CATEGORY_NAME_NOT_EXIST);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(CategoryNotProvidingDetail.class)
    public BaseErrorResponse handleCategoryNotProvidingDetail(final CategoryNotProvidingDetail e){
        log.error("[handle_CategoryException]");
        return new BaseErrorResponse(CATEGORYNAME_NOT_PROVIDING_DETAIL);
    }
}
