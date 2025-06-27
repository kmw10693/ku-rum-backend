package ku_rum.backend.global.handler;

import jakarta.annotation.Priority;
import ku_rum.backend.global.exception.email.DuplicateEmailException;
import ku_rum.backend.global.exception.user.*;
import ku_rum.backend.global.support.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class UserExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchUserException.class)
    public BaseErrorResponse handleNoSuchUserException(final NoSuchUserException e) {
        log.error("[handleNoSuchUserException]");
        return new BaseErrorResponse(NO_SUCH_USER);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MailSendException.class)
    public BaseErrorResponse handleMailSendException(final MailSendException e) {
        log.error("[handleMailSendException]");
        return new BaseErrorResponse(MAIL_SEND_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public BaseErrorResponse handleDuplicateEmailException(final DuplicateEmailException e) {
        log.error("[handleDuplicateEmailException]");
        return new BaseErrorResponse(DUPLICATE_EMAIL);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateLoginIdException.class)
    public BaseErrorResponse handleDuplicateLoginIdException(final DuplicateLoginIdException e) {
        log.error("[handleDuplicateLoginIdException]");
        return new BaseErrorResponse(DUPLICATE_LOGIN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateStudentIdException.class)
    public BaseErrorResponse handleDuplicateStudentIdException(final DuplicateStudentIdException e) {
        log.error("[handleDuplicateStudentIdException]");
        return new BaseErrorResponse(DUPLICATE_STUDENT_ID);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateNicknameException.class)
    public BaseErrorResponse handleDuplicateNicknameException(final DuplicateNicknameException e) {
        log.error("[handleDuplicateNicknameException]");
        return new BaseErrorResponse(DUPLICATE_NICKNAME);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordException.class)
    public BaseErrorResponse handleInvalidPasswordException(final InvalidPasswordException e) {
        log.error("[handleDuplicateNicknameException]");
        return new BaseErrorResponse(INVALID_PASSWORD);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserMapBuildingNotFoundException.class)
    public BaseErrorResponse handleNoSuchUserMapBuildingException(final NoSuchUserException e) {
        log.error("[handleNoSuchUserMapBuildingException]");
        return new BaseErrorResponse(MATCHED_USER_BUILDING_ERROR);
    }
}
