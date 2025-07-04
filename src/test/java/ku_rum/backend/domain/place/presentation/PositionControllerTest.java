package ku_rum.backend.domain.place.presentation;

import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.place.application.PositionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class PositionControllerTest extends RestDocsTestSupport {

    @MockBean
    private PositionService positionService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @DisplayName("현재 유저의 위치 공유여부를 확인한다.")
    @Test
    @WithMockUser
    void getCurrentPositionStatus() throws Exception {
        //given


        //when

        //then
    }
}
