package ku_rum.backend.domain.common.wein.application;

import ku_rum.backend.domain.common.wein.dto.request.WeinLoginRequest;
import ku_rum.backend.global.exception.wein.WeinException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WeinServiceTest {

    @InjectMocks
    private WeinService weinService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpHeaders headers;

    @Mock
    private CloseableHttpClient httpClient;

    @Test
    void loginToWein_ExceptionHandling() {
        // given
        WeinLoginRequest request = new WeinLoginRequest("testUser", "testPass");

        // when & then
        assertThrows(WeinException.class, () -> weinService.loginToWein(request));
    }

    @Test
    void createRequestBody_CorrectlyCreatesForm() {
        // given
        WeinLoginRequest request = new WeinLoginRequest("testUser", "testPass");

        // when
        MultiValueMap<String, String> requestBody = weinService.createRequestBody(request);

        // then
        assertEquals("testUser", requestBody.getFirst("userId"));
        assertEquals("testPass", requestBody.getFirst("pw"));
        assertEquals("", requestBody.getFirst("rtnUrl"));
    }
}