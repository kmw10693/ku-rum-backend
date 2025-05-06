package ku_rum.backend.domain.auth.dto.response;

import ku_rum.backend.domain.user.dto.response.TokenResponse;
import ku_rum.backend.domain.user.dto.response.UserResponse;

public record AuthResponse(TokenResponse tokenResponse, UserResponse userResponse) {

    public static AuthResponse of(TokenResponse tokenResponse, UserResponse userResponse) {
        return new AuthResponse(tokenResponse, userResponse);
    }

}
