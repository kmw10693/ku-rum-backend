package ku_rum.backend.domain.rank.application;

import java.util.List;
import java.util.Optional;
import ku_rum.backend.domain.place.domain.SubPlace;
import ku_rum.backend.domain.rank.application.response.GetPlaceUserRankResponse;
import ku_rum.backend.domain.rank.domain.PlaceRank;
import ku_rum.backend.domain.rank.domain.repository.PlaceRankRepository;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankService {

    private final PlaceRankRepository placeRankRepository;
    private final UserService userService;

    /**
     * 유저 장소 공유 랭킹 조회(3개)
     *
     * @param userDetails
     * @return
     */
    public List<GetPlaceUserRankResponse> getPlaceUserRank(final CustomUserDetails userDetails) {
        User user = userService.getUser();
        return placeRankRepository.findTop3ByUserOrderByCountDesc(user).stream()
                .map(GetPlaceUserRankResponse::from)
                .toList();
    }

    /**
     * 유저 장소 랭킹 업데이트
     *
     * @param user
     * @param subPlace
     */
    @Transactional
    public void updateRank(User user, SubPlace subPlace) {
        Optional<PlaceRank> optionalRank = placeRankRepository.findByUserAndSubPlace(user, subPlace);
        if (optionalRank.isPresent()) {
            optionalRank.get().increaseCount();
            return;
        }

        PlaceRank rank = PlaceRank.builder()
                .count(1)
                .user(user)
                .subPlace(subPlace)
                .build();
        placeRankRepository.save(rank);
    }
}
