package ku_rum.backend.domain.user.dto.request;

public record UserLocationRequest(
        double latitude,
        double longitude
) {
}
