package ku_rum.backend.domain.common.mail.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailSendSetting {
    MAIL_SEND_INFO("쿠룸 이메일 인증 번호",
            4,
            "AuthCode ",
            1800000,
            "메일이 성공적으로 전송되었습니다.");

    private final String TITLE;
    private final int CODE_LENGTH;
    private final String AUTH_CODE_PREFIX;
    private final long AUTH_EXPIRED_MILLS;
    private final String SUCCESS_MESSAGE;

}
