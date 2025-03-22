package ku_rum.backend.domain.common.mail.domain;

import ku_rum.backend.global.exception.user.MailSendException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.INVALID_AUTH_CODE;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MailAuth {
    private final String email;
    private final String authCode;

    public static MailAuth create(String email, int codeLength) {
        return new MailAuth(email, generateCode(codeLength));
    }

    public static MailAuth of(String email, String authCode) {
        return new MailAuth(email, authCode);
    }

    public void isValid(String inputCode) {
        if (!this.authCode.equals(inputCode)) {
            throw new MailSendException(INVALID_AUTH_CODE);
        }
    }

    private static String generateCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
