package ku_rum.backend.domain.common.firebase.presentation;

import ku_rum.backend.domain.common.firebase.application.NotificationService;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/push")
public class NotificationApiController {
    private final NotificationService notificationService;

    @PostMapping("/register")
    public BaseResponse<String> register(@RequestBody String token) {
        notificationService.register(token);
        return BaseResponse.ok("사용자 토큰 서버에 저장 완료");
    }

}
