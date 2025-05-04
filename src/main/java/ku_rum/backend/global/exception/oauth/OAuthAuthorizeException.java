package ku_rum.backend.global.exception.oauth;

import ku_rum.backend.global.support.status.ResponseStatus;

public class OAuthAuthorizeException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public OAuthAuthorizeException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public OAuthAuthorizeException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
