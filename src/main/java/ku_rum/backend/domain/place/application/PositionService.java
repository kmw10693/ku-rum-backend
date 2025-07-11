package ku_rum.backend.domain.place.application;

import java.util.Optional;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.domain.Position;
import ku_rum.backend.domain.place.domain.repository.PlaceRepository;
import ku_rum.backend.domain.place.domain.repository.PositionRepository;
import ku_rum.backend.domain.place.dto.request.CurrentPositionConfirmRequest;
import ku_rum.backend.domain.place.dto.request.CurrentPositionRequest;
import ku_rum.backend.domain.place.dto.response.CurrentPositionConfirmResponse;
import ku_rum.backend.domain.place.dto.response.CurrentPositionResponse;
import ku_rum.backend.domain.place.dto.response.CurrentPositionStatusResponse;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.exception.global.GlobalException;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.support.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final PlaceRepository placeRepository;
    private final UserService userService;

    /**
     * 사용자 위치 공유 여부 확인
     *
     * @param userDetails 사용자 인증 정보
     * @return response 객체
     */
    public CurrentPositionStatusResponse getCurrentPositionStatus(CustomUserDetails userDetails) {
        User user = userService.getUser();
        if (positionRepository.existsPositionByUser(user)) {

            return new CurrentPositionStatusResponse(true);
        }
        return new CurrentPositionStatusResponse(false);
    }

    /**
     * 사용자 위치값 반환 가장 근접한 건물 반환
     *
     * @param userDetails 사용자 인증정보
     * @param request     request 객체
     * @return response 객체
     */
    public CurrentPositionResponse getCurrentPosition(CustomUserDetails userDetails,
                                                      CurrentPositionRequest request) {
        Place findPlace = placeRepository.findNearestPlace(request.latitude(),
                request.longitude());
        return new CurrentPositionResponse(findPlace.getName());
    }

    /**
     * 사용자 위치 값 저장 이미 있는 경우 업데이트
     *
     * @param userDetails 사용자 인증정보
     * @param request     request 객체
     * @return response 객체
     */
    @Transactional
    public CurrentPositionConfirmResponse confirmCurrentPosition(CustomUserDetails userDetails,
                                                                 CurrentPositionConfirmRequest request) {
        User user = userService.getUser();
        Optional<Position> positionOptional = positionRepository.findPositionByUser(user);
        Place place = findOneByName(request.placeName());

        if (positionOptional.isPresent()) {
            Position position = positionOptional.get();
            position.update(place);
            return new CurrentPositionConfirmResponse(position.getPlace().getName());
        }

        Position position = Position.builder()
                .user(user)
                .place(place)
                .build();
        Position savePosition = positionRepository.save(position);
        return new CurrentPositionConfirmResponse(savePosition.getPlace().getName());
    }

    /**
     * 공유 취소
     *
     * @param userDetails 사용자 인증정보
     */
    public void disableSharingPosition(CustomUserDetails userDetails) {
        User user = userService.getUser();
        positionRepository.deleteByUser(user);
    }

    /**
     * place 조회
     *
     * @param name 이름(건물, k-cube ...)
     * @return place 엔티티
     */
    private Place findOneByName(String name) {
        return placeRepository.findOneByName(name)
                .orElseThrow(() -> new GlobalException(BaseExceptionResponseStatus.PLACE_NOT_FOUND));
    }
}