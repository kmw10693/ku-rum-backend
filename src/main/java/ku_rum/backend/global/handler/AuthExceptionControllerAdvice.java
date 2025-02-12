package ku_rum.backend.global.handler;

import jakarta.annotation.Priority;
import ku_rum.backend.global.exception.user.DuplicateEmailException;
import ku_rum.backend.global.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.DUPLICATE_EMAIL;
import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.LOGIN_ERROR;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class AuthExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadCredentialsException.class)
    public BaseErrorResponse handleBadCredentialsException(final BadCredentialsException e) {
        log.error("[handleBadCredentialsException]");
        return new BaseErrorResponse(LOGIN_ERROR);
    }
}
