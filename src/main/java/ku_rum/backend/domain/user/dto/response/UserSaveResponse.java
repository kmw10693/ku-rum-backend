package ku_rum.backend.domain.user.dto.response;

import ku_rum.backend.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record UserSaveResponse(Long id) {
    public static UserSaveResponse from(User user) {
        return new UserSaveResponse(user.getId());
    }
}
