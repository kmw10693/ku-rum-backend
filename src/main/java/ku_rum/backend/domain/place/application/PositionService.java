package ku_rum.backend.domain.place.application;

import ku_rum.backend.domain.place.domain.repository.PositionRepository;
import ku_rum.backend.domain.place.dto.response.CurrentPositionStatusResponse;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final UserService userService;

    /**
     * 사용자 위치 공유 여부 확인
     * @param userDetails 사용자 인증 정보
     * @return response 객체
     */
    public CurrentPositionStatusResponse getCurrentPositionStatus(CustomUserDetails userDetails){
        User user = userService.getUser();
        if(positionRepository.existsPositionByUser(user)){
            return new CurrentPositionStatusResponse(true);
        }
        return new CurrentPositionStatusResponse(false);
    }
}
