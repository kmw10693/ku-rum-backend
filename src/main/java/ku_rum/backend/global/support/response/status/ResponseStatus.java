package ku_rum.backend.global.support.response.status;

import org.springframework.http.HttpStatus;

public interface ResponseStatus {

    int getCode();
    HttpStatus getStatus();
    String getMessage();
}
