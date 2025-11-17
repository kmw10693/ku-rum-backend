package ku_rum.backend.domain.oauth.handler;

import com.github.dockerjava.api.exception.BadRequestException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ku_rum.backend.domain.oauth.domain.PreSignupPrincipal;
import ku_rum.backend.domain.oauth.util.AppProperties;
import ku_rum.backend.domain.oauth.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static ku_rum.backend.domain.oauth.handler.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AppProperties appProperties;
    private final TempTokenProvider tempTokenProvider;             // 기존 가입자용 임시토큰
    private final PreSignupTokenProvider preSignupTokenProvider;   // 신규: 프리사인업 토큰
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("응답이 이미 커밋되었습니다. " + targetUrl + "로 리다이렉트 할 수 없습니다");
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("승인되지 않은 리다이렉션 URI입니다");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        Object principal = authentication.getPrincipal();

        if (principal instanceof PreSignupPrincipal pre) {
            // 미가입자 —> 프리사인업 토큰 발급
            String preToken = preSignupTokenProvider.create(pre);
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("needSignup", true)
                    .queryParam("token", preToken)
                    .build().toUriString();
        }

        // 기존 가입자 —> 임시토큰(=userId 바인딩) 발급 (기존 교환 플로우 유지)
        String tempToken = tempTokenProvider.createTempToken(authentication);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("needSignup", false)
                .queryParam("token", tempToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
