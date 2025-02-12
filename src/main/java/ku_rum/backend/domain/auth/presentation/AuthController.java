package ku_rum.backend.domain.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ku_rum.backend.domain.auth.application.AuthService;
import ku_rum.backend.domain.auth.dto.request.LoginRequest;
import ku_rum.backend.domain.auth.dto.request.ReissueRequest;
import ku_rum.backend.global.response.BaseResponse;
import ku_rum.backend.global.security.jwt.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.LOGOUT_SUCCESS;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<TokenResponse> login(@Valid @RequestBody LoginRequest authRequest) {
        return BaseResponse.ok(authService.login(authRequest));
    }

    @PatchMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return BaseResponse.ok(LOGOUT_SUCCESS.getMessage());
    }

    @PatchMapping("/reissue")
    public BaseResponse<TokenResponse> reissue(@Valid @RequestBody ReissueRequest reissueRequest) {
        return BaseResponse.ok(authService.reissue(reissueRequest));
    }
}
