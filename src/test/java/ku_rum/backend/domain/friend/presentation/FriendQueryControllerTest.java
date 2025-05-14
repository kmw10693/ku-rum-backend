package ku_rum.backend.domain.friend.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.friend.application.FriendQueryService;
import ku_rum.backend.domain.friend.dto.response.FriendListResponse;
import ku_rum.backend.domain.friend.dto.response.ReceivedFriendResponse;
import ku_rum.backend.global.batch.BatchScheduler;
import ku_rum.backend.util.RestDocsFieldSnippets;
import ku_rum.backend.util.RestDocsTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class FriendQueryControllerTest extends RestDocsTestSupport {

    @MockBean
    private FriendQueryService friendQueryService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private BatchScheduler batchScheduler;

    @Test
    @DisplayName("친구 목록 조회 API")
    @WithMockUser
    void getFriendList() throws Exception {
        // given
        List<FriendListResponse> mockList = List.of(
                new FriendListResponse(1L, "친구1"),
                new FriendListResponse(2L, "친구2")
        );
        when(friendQueryService.getFriendList()).thenReturn(mockList);

        // when & then
        mockMvc.perform(get("/api/v1/friends/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(RestDocsTestUtils.expectCommonSuccess())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nickname").value("친구1"))
                .andDo(restDocs.document(
                        resource(ResourceSnippetParameters.builder()
                                .tag("친구 관련 API")
                                .description("친구 목록 조회")
                                .responseFields(RestDocsFieldSnippets.withDataFields(List.of(
                                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("친구 ID"),
                                        fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("친구 닉네임")
                                )))
                                .build())
                ));
    }

    @Test
    @DisplayName("받은 친구 요청 목록 조회 API")
    @WithMockUser
    void getReceivedFriendRequests() throws Exception {
        // given
        List<ReceivedFriendResponse> mockList = List.of(
                new ReceivedFriendResponse(10L, 3L, "요청자A"),
                new ReceivedFriendResponse(11L, 4L, "요청자B")
        );
        when(friendQueryService.getReceivedPendingRequests()).thenReturn(mockList);

        // when & then
        mockMvc.perform(get("/api/v1/friends/requests/received"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(RestDocsTestUtils.expectCommonSuccess())
                .andExpect(jsonPath("$.data[0].requestId").value(10))
                .andExpect(jsonPath("$.data[0].fromUserId").value(3))
                .andExpect(jsonPath("$.data[0].fromUserNickname").value("요청자A"))
                .andDo(restDocs.document(
                        resource(ResourceSnippetParameters.builder()
                                .tag("친구 관련 API")
                                .description("받은 친구 요청 목록 조회")
                                .responseFields(RestDocsFieldSnippets.withDataFields(List.of(
                                        fieldWithPath("data[].requestId").type(JsonFieldType.NUMBER).description("요청 ID"),
                                        fieldWithPath("data[].fromUserId").type(JsonFieldType.NUMBER).description("보낸 사람 ID"),
                                        fieldWithPath("data[].fromUserNickname").type(JsonFieldType.STRING).description("보낸 사람 닉네임")
                                )))
                                .build())
                ));
    }
}