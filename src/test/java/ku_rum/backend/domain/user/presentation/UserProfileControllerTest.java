package ku_rum.backend.domain.user.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.dto.request.NicknameChangeRequest;
import ku_rum.backend.domain.user.dto.request.ResetAccountRequest;
import ku_rum.backend.global.security.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserProfileControllerTest extends RestDocsTestSupport {

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("닉네임 변경 성공")
    @WithMockUser
    void changeNickname_Success() throws Exception {
        // Given
        NicknameChangeRequest request = new NicknameChangeRequest("안녕안녕");
        CustomUserDetails userDetails = CustomUserDetails.of(1L, "testUser", AuthorityUtils.createAuthorityList("ROLE_USER"), "test12345");
        String requestBody = new ObjectMapper().writeValueAsString(request);

        // When & Then
        mockMvc.perform(patch("/api/v1/users/nickname")
                        .header("Bearer", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyUEsiOjEsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQwMjQyNjQxLCJleHAiOjE3NDAyNDQ0NDF9.kLSMBLWdvIvrBpGJdOigSKjxMIab0cV06xFjSpwrq70")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("닉네임이 변경되었습니다."))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .description("닉네임 변경 API")
                                .requestHeaders(
                                        headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
                                )
                                .requestFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("변경할 닉네임")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태 (OK)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("응답 완료 메시지")
                                )
                                .build())));

        // Verify
        verify(userService, times(1)).changeNickname(any(NicknameChangeRequest.class));
    }

    @Test
    @DisplayName("비밀번호를 변경한다.")
    @WithMockUser
    void resetAccount() throws Exception {
        // given
        ResetAccountRequest resetAccountRequest = new ResetAccountRequest("user123", "test1234", "test12345");

        // when then
        mockMvc.perform(post("/api/v1/users/reset-account")
                        .header("Bearer", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyUEsiOjEsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQwMjQyNjQxLCJleHAiOjE3NDAyNDQ0NDF9.kLSMBLWdvIvrBpGJdOigSKjxMIab0cV06xFjSpwrq70")
                        .content(objectMapper.writeValueAsString(resetAccountRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .description("비밀번호 변경")
                                .requestHeaders(
                                        headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
                                )
                                .requestFields(
                                        fieldWithPath("loginId")
                                                .type(JsonType.STRING)
                                                .description("비밀번호 변경할 아이디")
                                                .attributes(constraints("비밀번호를 변경할 아이디입니다.")),
                                        fieldWithPath("prevPassword")
                                                .type(JsonType.STRING)
                                                .description("기존 비밀번호")
                                                .attributes(constraints("기존 비밀번호입니다.")),
                                        fieldWithPath("newPassword")
                                                .type(JsonType.STRING)
                                                .description("새 비밀번호")
                                                .attributes(constraints("새 비밀번호입니다."))
                                )
                                .responseFields(
                                        fieldWithPath("code")
                                                .type(JsonType.NUMBER)
                                                .description("성공시 반환 코드 (200)"),
                                        fieldWithPath("status")
                                                .type(JsonType.STRING)
                                                .description("성공시 상태 값 (OK)"),
                                        fieldWithPath("message")
                                                .type(JsonType.STRING)
                                                .description("성공 시 메시지 값 (OK)"),
                                        fieldWithPath("data")
                                                .type(JsonType.STRING)
                                                .description("성공 시 '아이디/비밀번호가 변경되었습니다.' 반환")
                                ).build())));
    }
}
