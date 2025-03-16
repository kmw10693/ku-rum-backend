package ku_rum.backend.domain.user.dto.response;

public record LoginIdResponse(String loginId) {

    public static LoginIdResponse of(String loginId) {
        return new LoginIdResponse(loginId);
    }
}
