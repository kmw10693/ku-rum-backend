package ku_rum.backend.domain.common.mail.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.common.mail.application.MailService;
import ku_rum.backend.domain.common.mail.dto.request.MailSendRequest;
import ku_rum.backend.domain.common.mail.dto.request.MailVerificationRequest;
import ku_rum.backend.domain.common.mail.dto.response.MailVerificationResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static ku_rum.backend.domain.common.mail.domain.MailSendSetting.MAIL_SEND_INFO;

@RestController
@RequestMapping("/api/v1/mails")
@RequiredArgsConstructor
@Validated
public class MailController {
    private final MailService mailService;

    @PostMapping("/auth-codes")
    public BaseResponse<String> authCode(@RequestBody @Valid final MailSendRequest mailSendRequest) {
        mailService.sendCodeToEmail(mailSendRequest);
        return BaseResponse.ok(MAIL_SEND_INFO.getSUCCESS_MESSAGE());
    }

    @PostMapping("/verification_codes")
    public BaseResponse<MailVerificationResponse> verificationCode(@RequestBody @Valid final MailVerificationRequest mailVerificationRequest) {
        return BaseResponse.ok(mailService.verifiedCode(mailVerificationRequest));
    }

}
