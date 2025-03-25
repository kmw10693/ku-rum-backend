package ku_rum.backend.domain.user.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.user.application.UserValidator;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.common.mail.dto.request.EmailValidationRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.domain.user.dto.response.UserSaveResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static ku_rum.backend.domain.user.domain.UserMessage.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

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
        userValidator.validateDuplicateEmail(emailValidationRequest.email());
        return BaseResponse.ok(VALID_EMAIL_MESSAGE.getMessage());
    }

    /**
     * 아이디 중복 확인 API
     *
     * @param value
     * @return
     */
    @GetMapping("/check-id")
    public BaseResponse<String> checkDuplicateId(@RequestParam("value") final String value) {
        userValidator.validateDuplicateLoginId(value);
        return BaseResponse.ok(VALID_LOGINID_MESSAGE.getMessage());
    }

    /**
     * 닉네임 중복 확인 API
     *
     * @param value
     * @return
     */
    @GetMapping("/check-nickname")
    public BaseResponse<String> checkDuplicateNickname(@RequestParam("value") final String value) {
        userValidator.validateNickname(value);
        return BaseResponse.ok(VALID_NICKNAME_MESSAGE.getMessage());
    }

    /**
     * 학번 중복 확인 API
     *
     * @param value
     * @return
     */
    @GetMapping("/check-studentId")
    public BaseResponse<String> checkDuplicateStudentId(@RequestParam("value") final String value) {
        userValidator.validateDuplicateStudentId(value);
        return BaseResponse.ok(VALID_STUDENTID_MESSAGE.getMessage());
    }
}


