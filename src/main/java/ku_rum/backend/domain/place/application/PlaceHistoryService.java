package ku_rum.backend.domain.place.application;

import java.util.List;
import ku_rum.backend.domain.place.application.response.SearchPlaceHistoryResponse;
import ku_rum.backend.domain.place.domain.PlaceHistory;
import ku_rum.backend.domain.place.domain.repository.PlaceHistoryRepository;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@Transactional(readOnly = true)
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceHistoryService {

    private final PlaceHistoryRepository placeHistoryRepository;
    private final UserService userService;

    /**
     * 지도 검색 히스토리 조회
     *
     * @param userDetails
     * @return
     */
    public List<SearchPlaceHistoryResponse> searchPlaceHistory(final CustomUserDetails userDetails) {
        User user = userService.getUser();
        return placeHistoryRepository.findTop5ByUserOrderByModifiedAtDesc(user).stream()
                .map(SearchPlaceHistoryResponse::from)
                .toList();
    }


    /**
     * 검색 히스토리 업데이트
     *
     * @param query
     * @param userDetails
     */
    @Transactional
    public void updatePlaceHistory(final String query, final CustomUserDetails userDetails) {
        User user = userService.getUser();
        placeHistoryRepository.findByNameAndUser(query, user)
                .ifPresentOrElse(
                        PlaceHistory::refreshModifiedAt,
                        () -> savePlaceHistory(query, user)
                );
    }

    /**
     * 유저 검색 히스토리 삭제
     */
    @Transactional
    public void deletePlaceHistory(final Long placeHistoryId, final CustomUserDetails userDetails) {
        User user = userService.getUser();
        placeHistoryRepository.deleteByPlaceHistoryIdAndUser(placeHistoryId, user);
    }

    /**
     * 유저 검색 히스토리 모두 삭제
     */
    @Transactional
    public void deletePlaceHistory(final CustomUserDetails userDetails) {
        User user = userService.getUser();
        placeHistoryRepository.deleteByUser(user);
    }

    /**
     * 검색 히스토리 저장
     *
     * @param query
     * @param user
     */
    private void savePlaceHistory(final String query, final User user) {
        PlaceHistory placeHistory = PlaceHistory.builder()
                .name(query)
                .user(user)
                .build();
        placeHistoryRepository.save(placeHistory);
    }
}
