package ku_rum.backend.domain.place.presentation;

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
public class PositionControllerTest {

    @MockBean
    private PositionService positionService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @DisplayName("로그아웃을 진행한다.")
    @Test
    @WithMockUser
    void getCurrentPositionStatus() throws Exception {
        //gi
    }
}
