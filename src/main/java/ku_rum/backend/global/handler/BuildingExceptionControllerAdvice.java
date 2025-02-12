package ku_rum.backend.global.handler;

import jakarta.annotation.Priority;
import ku_rum.backend.global.exception.building.BuildingNotFoundException;
import ku_rum.backend.global.exception.building.BuildingNotRegisteredException;
import ku_rum.backend.global.response.BaseErrorResponse;
import ku_rum.backend.global.response.status.BaseExceptionResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class BuildingExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(BuildingNotRegisteredException.class)
    public BaseErrorResponse handleNoBuildingRegisteredException(final BuildingNotRegisteredException e) {
        log.error("[handleNoBuildingRegisteredException]", e);
        return new BaseErrorResponse(NO_BUILDING_REGISTERED_CURRENTLY);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BuildingNotFoundException.class)
    public BaseErrorResponse handleNoBuildingFoundException(final BuildingNotFoundException e){
        log.error("[handleNoBuildingFoundException]", e);
        return new BaseErrorResponse(BUILDING_DATA_NOT_FOUND_BY_NAME);
    }
}
