package ku_rum.backend.domain.common.wein.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.reservation.dto.request.WeinLoginRequest;
import ku_rum.backend.domain.common.wein.application.WeinService;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus.WEIN_LOGIN_SUCCESS;

@RestController
@RequestMapping("/api/v1/wein")
@RequiredArgsConstructor
@Validated
public class WeinController {
    private final WeinService weinService;

    @PostMapping("/login")
    public BaseResponse<String> loginToWein(@RequestBody @Valid WeinLoginRequest weinLoginRequest) {
        weinService.loginToWein(weinLoginRequest);
        return BaseResponse.ok(WEIN_LOGIN_SUCCESS.getMessage());
    }
}
