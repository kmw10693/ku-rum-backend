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
            "/api/v1/users/validations/loginId",
            "/api/v1/users/weinlogin",
            "/api/v1/users/reset-account",
            "/api/v1/buildings/view/**",
            "/api/v1/buildings/view/add"
    )));

    private final List<String> authorities;

    AuthorizationList(List<String> authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities.toArray(new String[0]);
    }
}
