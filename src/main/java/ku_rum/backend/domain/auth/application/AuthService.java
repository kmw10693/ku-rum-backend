package ku_rum.backend.domain.auth.application;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import ku_rum.backend.domain.auth.dto.request.LoginRequest;
import ku_rum.backend.domain.auth.dto.request.ReissueRequest;
import ku_rum.backend.global.security.jwt.*;
import ku_rum.backend.global.security.jwt.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus.MALFORMED_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;
    private final TokenBlacklistService tokenBlacklistService;

    public TokenResponse login(LoginRequest authRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.loginId(),
                            authRequest.password()));
            return jwtTokenProvider.createToken(authenticate);
        } catch (AuthenticationException e) {
            log.error("유저에 대한 로그인 오류 발생: {}", authRequest.loginId(), e);
            throw new BadCredentialsException("[유저에 대한 로그인 오류 발생]");
        }
    }

    public void logout(HttpServletRequest request) {
        String token = validateAccessToken(request);
        Long userId = jwtTokenProvider.getUserId(token);

        long expiredAccessTokenTime = getExpiredAccessTokenTime(token);
        tokenBlacklistService.setBlackListInRedis(token, expiredAccessTokenTime, userId);
    }

    public TokenResponse reissue(ReissueRequest reissueRequest) {
        jwtTokenProvider.validateToken(reissueRequest.refreshToken());

        Authentication authenticate = jwtTokenProvider.getAuthentication(reissueRequest.refreshToken());
        CustomUserDetails principal = (CustomUserDetails) authenticate.getPrincipal();

        if (tokenBlacklistService.validateRefreshTokenInRedis(reissueRequest, principal))
            return jwtTokenProvider.createToken(authenticate);

        log.error("토큰 재발급 오류 발생: {}", MALFORMED_TOKEN.getMessage());
        throw new JwtException(MALFORMED_TOKEN.getMessage());
    }

    private long getExpiredAccessTokenTime(String token) {
        return jwtTokenProvider.getExpiredTime(token) - System.currentTimeMillis();
    }

    private String validateAccessToken(HttpServletRequest request) {
        String token = jwtTokenAuthenticationFilter.resolveToken(request);
        jwtTokenProvider.validateToken(token);
        return token;
    }
}
