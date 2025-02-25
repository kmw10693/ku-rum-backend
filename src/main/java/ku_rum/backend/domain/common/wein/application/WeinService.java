package ku_rum.backend.domain.common.wein.application;

import jakarta.validation.Valid;
import ku_rum.backend.domain.reservation.dto.request.WeinLoginRequest;
import ku_rum.backend.global.exception.wein.WeinException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus.LOGIN_FAILED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WeinService {

    public void loginToWein(@Valid final WeinLoginRequest weinLoginRequest) {
        CloseableHttpClient httpClient = getCloseableHttpClient();
        RestTemplate restTemplate = getRestTemplate(httpClient);
        HttpHeaders headers = getHeaders();

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(createRequestBody(weinLoginRequest), headers);
        log.info("Attempting login with userId: {} and password: {}", weinLoginRequest.getUserId(), weinLoginRequest.getPassword());

        try {
            ResponseEntity<String> response = getResponseEntity(restTemplate, requestEntity);
            getWeinLoginResponseBaseResponse(weinLoginRequest, response);
        } catch (Exception e) {
            log.error("Exception occurred during login: ", e);
            throw new WeinException(LOGIN_FAILED);
        }
    }

    private static ResponseEntity<String> getResponseEntity(RestTemplate restTemplate, HttpEntity<MultiValueMap<String, String>> requestEntity) {
        ResponseEntity<String> response = restTemplate.exchange(
                "https://wein.konkuk.ac.kr/common/user/loginProc.do",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return response;
    }

    private static void getWeinLoginResponseBaseResponse(WeinLoginRequest weinLoginRequest, ResponseEntity<String> response) {
        Optional<String> responseBodyOpt = Optional.ofNullable(response.getBody());
        if (responseBodyOpt.isPresent()) {
            String responseBody = responseBodyOpt.get();
            if (responseBody.contains("index.do")) {
                log.info("Login successful for userId: {}", weinLoginRequest.getUserId());
            } else if (responseBody.contains("login.do")) {
                log.warn("Login failed for userId: {}", weinLoginRequest.getUserId());
                throw new WeinException(LOGIN_FAILED);
            }
        }
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private static RestTemplate getRestTemplate(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    private static CloseableHttpClient getCloseableHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRedirectStrategy(new DefaultRedirectStrategy()) // 기본 리다이렉션 전략
                .setDefaultCookieStore(new BasicCookieStore())       // 세션 유지를 위한 쿠키 관리
                .build();
        return httpClient;
    }

    private MultiValueMap<String, String> createRequestBody(WeinLoginRequest weinLoginRequest) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("userId", weinLoginRequest.getUserId());
        requestBody.add("pw", weinLoginRequest.getPassword());
        requestBody.add("rtnUrl", ""); // 리다이렉트 후 이동할 URL 지정, 필요시 수정
        return requestBody;
    }

}
