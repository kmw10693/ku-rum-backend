package ku_rum.backend.global.handler;

import ku_rum.backend.global.exception.place.PlaceImageNotFoundException;
import ku_rum.backend.global.exception.place.PlaceNotFoundException;
import ku_rum.backend.global.support.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.PLACE_NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class PlaceExceptionControllerAdvice {

    @ExceptionHandler(PlaceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseErrorResponse handlePlaceNotFoundException(PlaceNotFoundException e) {
        log.error("[PlaceNotFoundException] {}", e.getMessage());
        return new BaseErrorResponse(PLACE_NOT_FOUND);
    }

    @ExceptionHandler(PlaceImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseErrorResponse handlePlaceImageNotFoundException(PlaceImageNotFoundException e) {
        log.error("[PlaceImageNotFoundException] {}", e.getMessage());
        return new BaseErrorResponse(PLACE_NOT_FOUND);
    }

}
