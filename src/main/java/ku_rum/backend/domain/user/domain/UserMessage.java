package ku_rum.backend.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserMessage {

    VALID_EMAIL_MESSAGE("올바른 이메일 입니다."),
    SUCCESS_RESET_PASSWORD("비밀번호가 변경되었습니다."),
    VALID_NICKNAME_MESSAGE("올바른 닉네임 입니다."),
    VALID_STUDENTID_MESSAGE("올바른 학번 입니다."),
    VALID_LOGINID_MESSAGE("올바른 아이디 입니다."),
    SUCCESS_CHANGE_NICKNAME("닉네임이 변경되었습니다.");

    private final String message;
}
