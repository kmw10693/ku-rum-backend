package ku_rum.backend.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ku_rum.backend.global.response.status.ResponseStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"code", "status", "message", "data"})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponse<T> implements ResponseStatus {

    final int code;
    final HttpStatus status;
    final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    private final T data;


    public BaseResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public static <T> BaseResponse<T> of(HttpStatus status, String message, T data) {
        return new BaseResponse<>(status, message, data);
    }

    public static <T> BaseResponse<T> of(HttpStatus status, T data) {
        return of(status, status.name(), data);
    }

    public static <T> BaseResponse<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
