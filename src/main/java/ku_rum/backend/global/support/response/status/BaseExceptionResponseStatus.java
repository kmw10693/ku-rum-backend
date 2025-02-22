package ku_rum.backend.global.support.response.status;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus {

    /**
     * 000: 서버 내부 오류
     */
    INTERNAL_SERVER_ERROR(000, HttpStatus.INTERNAL_SERVER_ERROR, "서버내부 오류입니다."),

    /**
     * 100: 요청 성공 (OK)
     */
    SUCCESS(100, HttpStatus.OK, "요청에 성공하였습니다."),
    SUCCESS_PROFILE_SET(100, HttpStatus.OK, "프로필 변경에 성공하였습니다."),
    LOGOUT_SUCCESS(100, HttpStatus.OK, "로그아웃이 정상적으로 완료되었습니다."),
    WEIN_LOGIN_SUCCESS(100, HttpStatus.OK,"위인전 로그인에 성공하였습니다"),
    BOOKMARK_SUCCESS(100, HttpStatus.OK, "북마크에 성공하였습니다"),

    /**
     * 200: Request (BAD_REQUEST)
     */
    BAD_REQUEST(200, HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
    URL_NOT_FOUND(201, HttpStatus.BAD_REQUEST, "유효하지 않은 URL 입니다."),
    METHOD_NOT_ALLOWED(202, HttpStatus.METHOD_NOT_ALLOWED, "해당 URL에서는 지원하지 않는 HTTP Method 입니다."),

    /**
     * 300: User
     */
    DUPLICATE_LOGIN(300, HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    DUPLICATE_STUDENT_ID(301, HttpStatus.BAD_REQUEST, "이미 존재하는 학번입니다."),
    MAIL_SEND_EXCEPTION(302, HttpStatus.INTERNAL_SERVER_ERROR, "인증 메일 전송에 오류가 발생했습니다."),
    INVALID_AUTH_CODE_GENERATION(303, HttpStatus.INTERNAL_SERVER_ERROR, "인증 번호 4자리 수 생성에 오류가 발생했습니다."),
    NO_SUCH_USER(304, HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    DUPLICATE_EMAIL(305, HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),

    /**
     * 400: Department
     */
    NO_SUCH_DEPARTMENT(400, HttpStatus.BAD_REQUEST, "존재하지 않는 학과명입니다."),

    /**
     * 500: Building 오류
     */
    BUILDING_DATA_NOT_FOUND_BY_NAME(500, HttpStatus.NOT_FOUND, "유효하지 않은 건물 명칭입니다."),
    BUILDING_DATA_NOT_FOUND_BY_NUMBER(501, HttpStatus.NOT_FOUND, "유효하지 않은 건물 번호입니다."),
    NO_BUILDING_REGISTERED_CURRENTLY(502, HttpStatus.NO_CONTENT, "등록된 건물이 없습니다."),

    /**
     * 600: Category
     */
    CATEGORY_NAME_NOT_EXIST(600, HttpStatus.NOT_FOUND, "존재하지 않는 카테고리명 입니다."),
    CATEGORYNAME_NOT_PROVIDING_DETAIL(601, HttpStatus.NO_CONTENT, "디테일을 제공하는 카테고리명이 아닙니다."),

    /**
     * 700: Friends
     */
    NO_FRIENDS_FOUND(700, HttpStatus.NO_CONTENT, "친구 목록에 친구가 존재하지 않습니다."),
    DUPLICATE_FRIENDS(700, HttpStatus.NO_CONTENT, "이미 존재하는 친구입니다."),

    /**
     * 800: Notice
     */
    NO_SUCH_NOTICE(800, HttpStatus.BAD_REQUEST, "해당 공지사항은 존재하지 않습니다."),
    DUPLICATE_NOTICE(801, HttpStatus.BAD_REQUEST, "이미 북마크된 공지사항입니다."),
    /**
     * 900: Server, DataBase
     */
    SERVER_ERROR(900, HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생하였습니다."),
    DATABASE_ERROR(901, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스에서 오류가 발생하였습니다."),
    BAD_SQL_GRAMMAR(902, HttpStatus.INTERNAL_SERVER_ERROR, "SQL에 오류가 있습니다."),

    /**
     * 1000: Authorization
     */
    JWT_ERROR(1000, HttpStatus.UNAUTHORIZED, "JWT에서 오류가 발생하였습니다."),
    TOKEN_NOT_FOUND(1001, HttpStatus.BAD_REQUEST, "토큰이 HTTP Header에 없습니다."),
    UNSUPPORTED_TOKEN_TYPE(1002, HttpStatus.BAD_REQUEST, "지원되지 않는 토큰 형식입니다."),
    INVALID_TOKEN(1003, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    MALFORMED_TOKEN(1004, HttpStatus.UNAUTHORIZED, "토큰이 올바르게 구성되지 않았습니다."),
    EXPIRED_TOKEN(1005, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    TOKEN_MISMATCH(1006, HttpStatus.UNAUTHORIZED, "로그인 정보가 토큰 정보와 일치하지 않습니다."),
    LOGIN_ERROR(1007, HttpStatus.UNAUTHORIZED, "잘못된 아이디/비밀번호입니다."),

    /**
    * 1100: Wein
     */
    LOGIN_FAILED(1100, HttpStatus.UNAUTHORIZED, "위인전 로그인이 실패하였습니다.");

    private final int code;
    private final HttpStatus status;
    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
