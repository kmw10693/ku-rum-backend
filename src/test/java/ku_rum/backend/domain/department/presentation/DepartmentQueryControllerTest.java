package ku_rum.backend.domain.department.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.department.application.DepartmentQueryService;
import ku_rum.backend.domain.department.dto.CollegeDepartmentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class DepartmentQueryControllerTest extends RestDocsTestSupport {

    @MockBean
    private DepartmentQueryService departmentQueryService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Test
    @DisplayName("단일 컬리지명으로 학과 목록 조회 성공")
    @WithMockUser
    void getDepartment_success() throws Exception {
        // Given
        String collegeName = "Engineering";
        List<String> depts = List.of("Mechanical", "Electrical", "Computer Science");
        CollegeDepartmentResponse dto = new CollegeDepartmentResponse(depts);

        given(departmentQueryService.getDepartmentsByCollege(collegeName))
                .willReturn(dto);

        // When & Then
        mockMvc.perform(get("/api/v1/departments")
                        .param("collegeName", collegeName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.name[0]").value("Mechanical"))
                .andExpect(jsonPath("$.data.name[1]").value("Electrical"))
                .andExpect(jsonPath("$.data.name[2]").value("Computer Science"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("학과 관련 API")
                                        .description("컬리지 이름으로 학과 목록 조회")
                                        .queryParameters(
                                                parameterWithName("collegeName")
                                                        .description("조회할 컬리지 이름")
                                        )
                                        .responseFields(
                                                fieldWithPath("code")
                                                        .type(JsonFieldType.NUMBER)
                                                        .description("응답 코드 (200)"),
                                                fieldWithPath("status")
                                                        .type(JsonFieldType.STRING)
                                                        .description("응답 상태 (OK)"),
                                                fieldWithPath("message")
                                                        .type(JsonFieldType.STRING)
                                                        .description("응답 메시지"),
                                                fieldWithPath("data.name")
                                                        .type(JsonFieldType.ARRAY)
                                                        .description("컬리지 이름 리스트")
                                        ).build()
                        )
                ));
    }
}