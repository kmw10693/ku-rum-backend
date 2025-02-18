package ku_rum.backend.global.config.security;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum AuthorizationList {
    LIST(Collections.unmodifiableList(Arrays.asList(
            "/api/v1/auth/login",
            "/api/v1/users",
            "/docs/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/users/validations/loginId",
            "/api/v1/users/weinlogin",
            "/api/v1/users/join",
            "/api/v1/mails/auth-codes",
            "/api/v1/mails/verification_codes"
    )));

    private final List<String> authorities;

    AuthorizationList(List<String> authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities.toArray(new String[0]);
    }
}
