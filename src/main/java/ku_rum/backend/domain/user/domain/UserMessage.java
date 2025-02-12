package ku_rum.backend.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserMessage {

    VALID_EMAIL_MESSAGE("올바른 이메일 입니다."),
    SUCCESS_RESET_PASSWORD("아이디/비밀번호가 변경되었습니다.");

    private final String message;
}
