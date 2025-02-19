package ku_rum.backend.domain.user.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.reservation.dto.request.WeinLoginRequest;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.mail.dto.request.LoginIdValidationRequest;
import ku_rum.backend.domain.user.dto.request.ProfileChangeRequest;
import ku_rum.backend.domain.user.dto.request.ResetAccountRequest;
import ku_rum.backend.domain.user.dto.request.UserSaveRequest;
import ku_rum.backend.global.security.jwt.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class UserControllerTest extends RestDocsTestSupport {

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @DisplayName("신규 유저를 생성한다.")
    @Test
    @WithMockUser
    void createUser() throws Exception {
        //given
        UserSaveRequest request = UserSaveRequest.builder()
                .email("kmw106933@konkuk.ac.kr")
                .loginId("kmw106933")
                .password("password123")
                .department("컴퓨터공학부")
                .nickname("미미미누")
                .studentId("202112322")
                .build();

        // when then
        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .description("신규 유저 생성")
                                .requestFields(
                                fieldWithPath("loginId")
                                        .type(JsonType.STRING)
                                        .description("멤버 아이디")
                                        .attributes(constraints("아이디 입력은 필수입니다. 최소 6자 이상입니다.")),
                                fieldWithPath("email")
                                        .type(JsonType.STRING)
                                        .description("멤버 이메일")
                                        .attributes(constraints("@konkuk.ac.kr로 끝나야 합니다.")),
                                fieldWithPath("nickname")
                                        .type(JsonType.STRING)
                                        .description("멤버 닉네임")
                                        .attributes(constraints("닉네임 입력은 필수입니다. 최대 8자 이하입니다.")),
                                fieldWithPath("password")
                                        .type(JsonType.STRING)
                                        .description("멤버 패스워드")
                                        .attributes(constraints("비밀번호는 영어와 숫자를 포함해서 8자 이상 20자 이내로 입력해주세요.")),
                                fieldWithPath("studentId")
                                        .type(JsonType.STRING)
                                        .description("멤버 학번")
                                        .attributes(constraints("학번은 20으로 시작하고, 9자리여야 합니다.")),
                                fieldWithPath("department")
                                        .type(JsonType.STRING)
                                        .description("멤버 학과")
                                        .attributes(constraints("ex) 컴퓨터공학부"))
                        )
                                .responseFields(
                                fieldWithPath("code")
                                        .type(JsonType.STRING)
                                        .description("성공시 반환 코드 (200)"),
                                fieldWithPath("status")
                                        .type(JsonType.STRING)
                                        .description("성공시 상태 값 (OK)"),
                                fieldWithPath("message")
                                        .type(JsonType.STRING)
                                        .description("성공 시 메시지 (OK)")
                        ).build())));
    }


    @DisplayName("아이디 중복을 확인한다.")
    @Test
    void validateEmail() throws Exception {
        //given
        LoginIdValidationRequest loginIdValidationRequest = new LoginIdValidationRequest("kmw106933@naver.com");
        //when then
        mockMvc.perform(post("/api/v1/users/validations")
                        .content(objectMapper.writeValueAsString(loginIdValidationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("올바른 이메일 입니다."))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .description("아이디 중복 확인")
                                .requestFields(
                                fieldWithPath("loginId")
                                        .type(JsonType.STRING)
                                        .description("멤버 아이디")
                                        .attributes(constraints("아이디 입력은 필수입니다. 최소 6자 이상입니다."))
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
                                        .description("성공 시 '올바른 이메일 입니다.' 반환")
                        ).build())));
    }

    @Test
    @DisplayName("비밀번호를 변경한다.")
    @WithMockUser
    void resetAccount() throws Exception {
        // given
        ResetAccountRequest resetAccountRequest = new ResetAccountRequest("test1234");
        CustomUserDetails userDetails = CustomUserDetails.of(1L, "testUser",  AuthorityUtils.createAuthorityList("ROLE_USER"),"test12345");

        // when then
        mockMvc.perform(post("/api/v1/users/reset-account")
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
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
                                fieldWithPath("password")
                                        .type(JsonType.STRING)
                                        .description("새로 변경할 비밀번호")
                                        .attributes(constraints("새로 변경할 비밀번호입니다."))
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

    @Test
    @DisplayName("프로필 이미지를 변경한다.")
    @WithMockUser
    void changeProfile() throws Exception {
        // given
        ProfileChangeRequest profileChangeRequest = new ProfileChangeRequest("test.com");
        CustomUserDetails userDetails = CustomUserDetails.of(1L, "testUser",  AuthorityUtils.createAuthorityList("ROLE_USER"),"test12345");

        // when then
        mockMvc.perform(patch("/api/v1/users/profile")
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .content(objectMapper.writeValueAsString(profileChangeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("유저 API")
                                        .description("프로필 이미지 변경")
                                        .requestHeaders(
                                headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
                        )
                                        .requestFields(
                                fieldWithPath("imageUrl")
                                        .type(JsonType.STRING)
                                        .description("새로 변경할 프로필")
                                        .attributes(constraints("새로 변경할 프로필입니다."))
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
                                        .description("성공 시 반환 메시지")
                        ).build())));
    }
}