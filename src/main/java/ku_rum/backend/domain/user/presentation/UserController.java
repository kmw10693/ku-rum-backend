package ku_rum.backend.domain.user.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.user.dto.request.ProfileChangeRequest;
import ku_rum.backend.domain.user.dto.request.ResetAccountRequest;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.mail.dto.request.LoginIdValidationRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.domain.user.dto.response.UserSaveResponse;
import ku_rum.backend.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static ku_rum.backend.domain.user.domain.UserMessage.SUCCESS_RESET_PASSWORD;
import static ku_rum.backend.domain.user.domain.UserMessage.VALID_EMAIL_MESSAGE;
import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.SUCCESS_PROFILE_SET;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public BaseResponse<UserSaveResponse> join(@RequestBody @Valid final UserSaveRequest userSaveRequest) {
        return BaseResponse.ok(userService.saveUser(userSaveRequest));
    }

    @PostMapping("/validations")
    public BaseResponse<String> validateEmail(@RequestBody @Valid final LoginIdValidationRequest loginIdValidationRequest) {
        userService.ValidateUserId(loginIdValidationRequest);
        return BaseResponse.ok(VALID_EMAIL_MESSAGE.getMessage());
    }

    @PostMapping("/reset-account")
    public BaseResponse<String> resetAccount(@RequestBody @Valid final ResetAccountRequest resetAccountRequest) {
        userService.resetAccount(resetAccountRequest);
        return BaseResponse.ok(SUCCESS_RESET_PASSWORD.getMessage());
    }

    @PatchMapping("/profile")
    public BaseResponse<String> setProfile(@RequestBody @Valid final ProfileChangeRequest profileChangeRequest) {
        userService.setProfile(profileChangeRequest);
        return BaseResponse.ok(SUCCESS_PROFILE_SET.getMessage());
    }
}
