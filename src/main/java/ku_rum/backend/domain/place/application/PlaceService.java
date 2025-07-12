package ku_rum.backend.domain.place.application;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import ku_rum.backend.domain.place.domain.CategoryChip;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.domain.PlaceImage;
import ku_rum.backend.domain.place.domain.repository.PlaceImageRepository;
import ku_rum.backend.domain.place.domain.repository.PlaceRepository;
import ku_rum.backend.domain.place.domain.repository.PositionRepository;
import ku_rum.backend.domain.place.dto.FriendUserDto;
import ku_rum.backend.domain.place.dto.response.SelectPlaceChipFriendListResponse;
import ku_rum.backend.domain.place.dto.response.SelectPlaceChipResponse;
import ku_rum.backend.global.exception.global.GlobalException;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.support.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PositionRepository positionRepository;
    private final PlaceImageRepository placeImageRepository;

    /**
     * 지도 칩 조회(회원 로직)
     *
     * @param userDetails
     * @return response 객체
     */
    public List<SelectPlaceChipResponse> selectChipWithUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            CategoryChip categoryChip) {

        if (categoryChip.equals(CategoryChip.FRIEND)) {
            List<Place> places = placeRepository.findByCategoryChip(CategoryChip.BUILDING);
            return selectFriendListWithUser(userDetails, places);
        }

        List<Place> places = placeRepository.findByCategoryChip(categoryChip);
        if (categoryChip.equals(CategoryChip.BUILDING)) {
            return selectBuildingChipWithUser(userDetails, places);
        }

        return findPlacesWithImages(places);
    }

    /**
     * 지도 칩 조회(비회원 로직)
     *
     * @return
     */
    public List<SelectPlaceChipResponse> selectChip(CategoryChip categoryChip) {
        if (categoryChip.equals(CategoryChip.FRIEND)) {
            throw new GlobalException(BaseExceptionResponseStatus.UNSUPPORTED_FRIEND_CHIP_ERROR);
        }

        List<Place> places = placeRepository.findByCategoryChip(categoryChip);

        Map<Long, List<PlaceImage>> placeImageMap = findPlaceImageMap(places);
        return places.stream()
                .map(place -> {
                    return SelectPlaceChipResponse.from(place, placeImageMap);
                })
                .toList();
    }

    /**
     * 빌딩 칩 조회(회원 로직), 빌딩 정보와 친구의 공유 정보를 함께 반환
     *
     * @param userDetails
     * @return response 객체
     */
    private List<SelectPlaceChipResponse> selectBuildingChipWithUser(
            @AuthenticationPrincipal CustomUserDetails userDetails, List<Place> places) {
        List<FriendUserDto> friendUserDtos = positionRepository.findPlaceByFriend(userDetails.getUserId());

        return mapPlacesWithFriends(places, friendUserDtos);
    }

    /**
     * 친구 칩 조회(회원 로직), 친구의 공유 정보를 위치값과 함께 반환
     *
     * @return response 객체
     */
    private List<SelectPlaceChipResponse> selectFriendListWithUser(
            @AuthenticationPrincipal CustomUserDetails userDetails, List<Place> places) {
        List<FriendUserDto> friendUserDtos = positionRepository.findPlaceByFriend(userDetails.getUserId());

        Set<Long> placeIdSet = friendUserDtos.stream()
                .map(FriendUserDto::placeId)
                .collect(Collectors.toSet());

        List<Place> filteredPlaces = places.stream()
                .filter(place -> placeIdSet.contains(place.getPlaceId()))
                .toList();

        return mapPlacesWithFriends(filteredPlaces, friendUserDtos);
    }

    /**
     * 장소와 공유 친구, 이미지 함께 조회
     *
     * @param places         장소 리스트
     * @param friendUserDtos 친구 DTO
     * @return
     */
    private List<SelectPlaceChipResponse> mapPlacesWithFriends(List<Place> places,
                                                               List<FriendUserDto> friendUserDtos) {
        Map<Long, List<PlaceImage>> placeImageMap = findPlaceImageMap(places);
        return places.stream()
                .map(place -> {
                    List<SelectPlaceChipFriendListResponse> matchedFriends = friendUserDtos.stream()
                            .filter(friend -> friend.placeId().equals(place.getPlaceId()))
                            .map(SelectPlaceChipFriendListResponse::from)
                            .toList();

                    return SelectPlaceChipResponse.from(place, matchedFriends, placeImageMap);
                })
                .toList();
    }

    /**
     * 장소들에 속해있는 이미지 map 반환
     *
     * @param places 장소 리스트
     * @return
     */
    private Map<Long, List<PlaceImage>> findPlaceImageMap(List<Place> places) {
        return placeImageRepository.findByPlaceIn(places).stream()
                .collect(Collectors.groupingBy(
                        pi -> pi.getPlace().getPlaceId(),
                        Collectors.toList()
                ));
    }

    /**
     * 장소 이미지와 함께 조회
     *
     * @param places 장소 리스트
     * @return
     */
    private List<SelectPlaceChipResponse> findPlacesWithImages(List<Place> places) {
        Map<Long, List<PlaceImage>> placeImageMap = findPlaceImageMap(places);
        return places.stream()
                .map(place -> {
                    return SelectPlaceChipResponse.from(place, placeImageMap);
                })
                .toList();
    }
}
