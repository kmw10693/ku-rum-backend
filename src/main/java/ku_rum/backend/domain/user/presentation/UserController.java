package ku_rum.backend.domain.user.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.user.dto.request.ProfileChangeRequest;
import ku_rum.backend.domain.user.dto.request.ResetAccountRequest;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.common.mail.dto.request.EmailValidationRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.domain.user.dto.response.UserSaveResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static ku_rum.backend.domain.user.domain.enums.UserMessage.*;
import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.SUCCESS_PROFILE_SET;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    /**
     * 회원 가입 API
     *
     * @param userSaveRequest
     * @return
     */
    @PostMapping
    public BaseResponse<UserSaveResponse> join(@RequestBody @Valid final UserSaveRequest userSaveRequest) {
        return BaseResponse.ok(userService.saveUser(userSaveRequest));
    }

    /**
     * 이메일 검증 API
     *
     * @param emailValidationRequest
     * @return
     */
    @PostMapping("/validations")
    public BaseResponse<String> validateEmail(@RequestBody @Valid final EmailValidationRequest emailValidationRequest) {
        userService.validateEmail(emailValidationRequest);
        return BaseResponse.ok(VALID_EMAIL_MESSAGE.getMessage());
    }

    /**
     * 비밀번호 초기화 API
     *
     * @param resetAccountRequest
     * @return
     */
    @PostMapping("/reset-account")
    public BaseResponse<String> resetAccount(@RequestBody @Valid final ResetAccountRequest resetAccountRequest) {
        userService.resetAccount(resetAccountRequest);
        return BaseResponse.ok(SUCCESS_RESET_PASSWORD.getMessage());
    }

    /**
     * 프로필 변경 API
     * @param profileChangeRequest
     * @return
     */
    @PatchMapping("/profile")
    public BaseResponse<String> setProfile(@RequestBody @Valid final ProfileChangeRequest profileChangeRequest) {
        userService.setProfile(profileChangeRequest);
        return BaseResponse.ok(SUCCESS_PROFILE_SET.getMessage());
    }

    /**
     * 아이디 중복 확인 API
     * @param value
     * @return
     */
    @GetMapping("/check-id")
    public BaseResponse<Boolean> checkDuplicateId(@RequestParam("value") final String value) {
        return BaseResponse.ok(userService.checkDuplicateId(value));
    }

    /**
     * 닉네임 중복 확인 API
     * @param value
     * @return
     */
    @GetMapping("/check-nickname")
    public BaseResponse<String> checkDuplicateNickname(@RequestParam("value") final String value) {
        userService.checkDuplicateNickname(value);
        return BaseResponse.ok(VALID_NICKNAME_MESSAGE.getMessage());
    }

    /**
     * 학번 중복 확인 API
     * @param value
     * @return
     */
    @GetMapping("/check-studentId")
    public BaseResponse<String> checkDuplicateStudentId(@RequestParam("value") final String value) {
        userService.checkDuplicateStudentId(value);
        return BaseResponse.ok(VALID_STUDENTID_MESSAGE.getMessage());
    }
}
