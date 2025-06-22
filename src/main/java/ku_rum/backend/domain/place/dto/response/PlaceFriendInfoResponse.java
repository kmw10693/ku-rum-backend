package ku_rum.backend.domain.place.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record PlaceFriendInfoResponse(
        String mainTitle,
        String subTitle,
        String text,
        BigDecimal latitude,
        BigDecimal longitude,
        List<Friend> friendList
) {
    public record Friend(
            String nickname,
            String profileUrl
    ) {}
}

