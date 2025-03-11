package ku_rum.backend.domain.common.firebase.presentation;

import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.common.firebase.application.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
class NotificationApiControllerTest extends RestDocsTestSupport {

    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("사용자 토큰을 서버에 저장한다.")
    @WithMockUser
    void register() throws Exception {
        // given
        String token = "sample-token";  // 테스트에 사용할 샘플 토큰

        // when & then
        mockMvc.perform(post("/api/v1/push/register")
                        .header("Bearer", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyUEsiOjEsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQwMjQyNjQxLCJleHAiOjE3NDAyNDQ0NDF9.kLSMBLWdvIvrBpGJdOigSKjxMIab0cV06xFjSpwrq70")
                        .content(token)  // 요청 본문에 토큰을 넣음
                        .contentType(MediaType.APPLICATION_JSON)  // JSON 형식으로 요청
                )
                .andDo(print())  // 요청과 응답을 출력
                .andExpect(status().isOk())  // 상태 코드 200을 예상
                .andDo(restDocs.document(
                        responseFields(  // 응답 필드 설명
                                fieldWithPath("code").description("성공 시 반환 코드 (200)"),
                                fieldWithPath("status").description("성공 시 상태 값 (OK)"),
                                fieldWithPath("message").description("성공 시 메시지 (사용자 토큰 서버에 저장 완료)"),
                                fieldWithPath("data").description("성공 시 '사용자 토큰 서버에 저장 완료' 반환")
                        )
                ));
    }
}