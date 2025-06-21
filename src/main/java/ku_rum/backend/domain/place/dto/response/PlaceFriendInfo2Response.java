package ku_rum.backend.domain.place.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record PlaceFriendInfo2Response(
        String nickname,
        List<Place> place
) {
    public record Place(
            String mainTitle,
            String subTitle,
            String text,
            BigDecimal latitude,
            BigDecimal longitude
    ) {}
}