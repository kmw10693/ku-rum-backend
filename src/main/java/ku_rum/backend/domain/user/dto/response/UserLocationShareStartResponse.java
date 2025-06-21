package ku_rum.backend.domain.user.dto.response;

public record UserLocationShareStartResponse(
        String placePointed,
        boolean active
) {
}
