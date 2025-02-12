package ku_rum.backend.global.handler;

import com.github.dockerjava.api.exception.BadRequestException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import jakarta.validation.ConstraintViolationException;
import ku_rum.backend.global.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.*;


@Slf4j
@RestControllerAdvice
public class BaseControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseErrorResponse handleServerException(final Exception e) {
        log.error("[handle InternalServerException]", e);
        return new BaseErrorResponse(SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class, NoHandlerFoundException.class, TypeMismatchException.class})
    public BaseErrorResponse handleBadRequest(final Exception e) {
        log.error("[handle BadRequest]", e);
        return new BaseErrorResponse(URL_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseErrorResponse handle_HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[handle_HttpRequestMethodNotSupportedException]", e);
        return new BaseErrorResponse(METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseErrorResponse handle_ConstraintViolationException(ConstraintViolationException e) {
        log.error("[handle_ConstraintViolationException]", e);
        return new BaseErrorResponse(BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public BaseErrorResponse handle_InternalServerError(InternalServerErrorException e) {
        log.error("[handle_InternalServerError]", e);
        return new BaseErrorResponse(INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseErrorResponse handle_RuntimeException(Exception e) {
        log.error("[handle_RuntimeException]", e);
        return new BaseErrorResponse(INTERNAL_SERVER_ERROR);
    }

}
