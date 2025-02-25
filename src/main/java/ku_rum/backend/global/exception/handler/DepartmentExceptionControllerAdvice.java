package ku_rum.backend.global.exception.handler;

import jakarta.annotation.Priority;
import ku_rum.backend.global.exception.department.NoSuchDepartmentException;
import ku_rum.backend.global.support.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus.NO_SUCH_DEPARTMENT;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class DepartmentExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchDepartmentException.class)
    public BaseErrorResponse handleNoSuchDepartmentException(final NoSuchDepartmentException e) {
        log.error("[handleNoSuchDepartmentException]");
        return new BaseErrorResponse(NO_SUCH_DEPARTMENT);
    }
}
