package ku_rum.backend.domain.user.dto.response;

public record UserResponse(Long id, String oauthId, String loginId, String email, String nickname, String studentId,
                           String imageUrl) {

    public static UserResponse of(Long id, String oauthId, String loginId, String email, String nickname, String studentId, String imageUrl) {
        return new UserResponse(id, oauthId, loginId, email, nickname, studentId, imageUrl);
    }
}
