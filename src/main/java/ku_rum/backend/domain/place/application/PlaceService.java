package ku_rum.backend.domain.place.application;

import java.util.List;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.place.domain.CategoryChip;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.domain.repository.PlaceRepository;
import ku_rum.backend.domain.place.domain.repository.PositionRepository;
import ku_rum.backend.domain.place.dto.FriendUserDto;
import ku_rum.backend.domain.place.dto.response.SelectPlaceChipFriendListResponse;
import ku_rum.backend.domain.place.dto.response.SelectPlaceChipResponse;
import ku_rum.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final FriendRepository friendRepository;
    private final PositionRepository positionRepository;

    /**
     * 지도 칩 조회(회원 로직)
     *
     * @param userDetails
     * @return
     */
    public List<SelectPlaceChipResponse> selectChipWithUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            CategoryChip category) {
        if (category.equals(CategoryChip.FRIEND)) {
            throw new RuntimeException("지원하지 않는 칩입니다");
        }

        List<Place> places = placeRepository.findByCategoryChip(category);

        if (category.equals(CategoryChip.BUILDING)) {
            return selectBuildingChipWithUser(userDetails, places);
        }

        return places.stream()
                .map(SelectPlaceChipResponse::from)
                .toList();
    }

    /**
     * 빌딩 칩 조회(회원 로직)
     *
     * @param userDetails
     * @return
     */
    private List<SelectPlaceChipResponse> selectBuildingChipWithUser(
            @AuthenticationPrincipal CustomUserDetails userDetails, List<Place> places) {
        List<FriendUserDto> friendUserDtos = positionRepository.findPlaceByFriend(userDetails.getUserId());

        return places.stream()
                .map(place -> {
                    List<SelectPlaceChipFriendListResponse> matchedFriends = friendUserDtos.stream()
                            .filter(friend -> friend.placeId().equals(place.getPlaceId()))
                            .map(SelectPlaceChipFriendListResponse::from)
                            .toList();

                    return SelectPlaceChipResponse.from(place, matchedFriends);
                })
                .toList();
    }
}
