package ku_rum.backend.global.handler;

import jakarta.annotation.Priority;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import ku_rum.backend.global.exception.wein.WeinException;
import ku_rum.backend.global.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.LOGIN_FAILED;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class WeinExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(WeinException.class)
    public BaseErrorResponse handleLoginFailedException(final WeinException e) {
        log.error("[handleLoginFailedException]");
        return new BaseErrorResponse(LOGIN_FAILED);
    }
}
