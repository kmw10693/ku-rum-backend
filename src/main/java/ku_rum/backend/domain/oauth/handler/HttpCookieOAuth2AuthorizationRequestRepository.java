package ku_rum.backend.domain.oauth.handler;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ku_rum.backend.domain.oauth.util.CookieUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import static ku_rum.backend.domain.oauth.util.CookieUtils.addCookie;
import static ku_rum.backend.domain.oauth.util.CookieUtils.deleteCookie;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements
        AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 180;

    /**
     * 로그인 수행시, OAuth2 인증 요청을 HTTP 쿠키에 저장한다. 리다이렉트 URI도 함께 저장한다.
     * * @param authorizationRequest 저장할 OAuth2 인증 요청
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    /**
     * 로그인 완료 후 우리 서비스로 복귀할 때 사용한다. 쿠키로부터 HTTP 요청에서 저장했던 OAuth2 인증 요청을 로드한다.
     * * @param request HTTP 요청
     *
     * @return 저장된 OAuth2 인증 요청, 없으면 null
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie.getValue(), OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    /**
     * 인증이 끝났으니 사용했던 쿠키들을 제거한다.
     * * @param request  HTTP 요청
     *
     * @param response HTTP 응답
     * @return 저장된 OAuth2 인증 요청, 없으면 null
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    /**
     * OAuth2 인증 요청과 관련된 쿠키들을 제거한다.
     * * @param request  HTTP 요청
     *
     * @param response HTTP 응답
     */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

    /**
     * 저장된 리다이렉트 URI를 가져온다.
     * * @param request HTTP 요청
     *
     * @return 저장된 리다이렉트 URI, 없으면 null
     */
    public String getRedirectUriAfterLogin(HttpServletRequest request) {
        return CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(null);
    }

}