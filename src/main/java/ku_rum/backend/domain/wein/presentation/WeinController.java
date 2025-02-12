package ku_rum.backend.domain.wein.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.reservation.dto.request.WeinLoginRequest;
import ku_rum.backend.domain.reservation.dto.response.WeinLoginResponse;
import ku_rum.backend.domain.wein.application.WeinService;
import ku_rum.backend.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wein")
@RequiredArgsConstructor
@Validated
public class WeinController {
    private final WeinService weinService;

    @PostMapping("/weinlogin")
    public BaseResponse<WeinLoginResponse> loginToWein(@RequestBody @Valid WeinLoginRequest weinLoginRequest) {
        return weinService.loginToWein(weinLoginRequest);

    }
}
