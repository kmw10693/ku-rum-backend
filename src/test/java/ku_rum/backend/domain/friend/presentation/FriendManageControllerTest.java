package ku_rum.backend.domain.friend.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.friend.application.FriendManageService;
import ku_rum.backend.global.batch.BatchScheduler;
import ku_rum.backend.util.RestDocsFieldSnippets;
import ku_rum.backend.util.RestDocsTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class FriendManageControllerTest extends RestDocsTestSupport {

    @MockBean
    private FriendManageService friendManageService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private BatchScheduler batchScheduler;

    @Test
    @DisplayName("친구 요청 API")
    @WithMockUser
    void sendFriendRequest() throws Exception {
        Long receiverId = 1L;
        doNothing().when(friendManageService).requestFriend(receiverId);

        mockMvc.perform(post("/api/v1/friends/request")
                        .param("receiverId", receiverId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(RestDocsTestUtils.expectCommonSuccess())
                .andDo(restDocs.document(
                        resource(ResourceSnippetParameters.builder()
                                .tag("친구 관련 API")
                                .description("친구 요청")
                                .responseFields(RestDocsFieldSnippets.COMMON_RESPONSE_FIELDS)
                                .build())
                ));
    }

    @Test
    @DisplayName("친구 요청 수락 API")
    @WithMockUser
    void acceptFriendRequest() throws Exception {
        Long requestId = 1L;
        doNothing().when(friendManageService).respondToFriend(requestId, true);

        mockMvc.perform(post("/api/v1/friends/accept")
                        .param("requestId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(RestDocsTestUtils.expectCommonSuccess())
                .andDo(restDocs.document(
                        resource(ResourceSnippetParameters.builder()
                                .tag("친구 관련 API")
                                .description("친구 요청 수락")
                                .responseFields(RestDocsFieldSnippets.COMMON_RESPONSE_FIELDS)
                                .build())
                ));
    }

    @Test
    @DisplayName("친구 요청 거절 API")
    @WithMockUser
    void rejectFriendRequest() throws Exception {
        Long requestId = 1L;
        doNothing().when(friendManageService).respondToFriend(requestId, false);

        mockMvc.perform(post("/api/v1/friends/reject")
                        .param("requestId", requestId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(RestDocsTestUtils.expectCommonSuccess())
                .andDo(restDocs.document(
                        resource(ResourceSnippetParameters.builder()
                                .tag("친구 관련 API")
                                .description("친구 요청 거절")
                                .responseFields(RestDocsFieldSnippets.COMMON_RESPONSE_FIELDS)
                                .build())
                ));
    }

    @Test
    @DisplayName("친구 요청 삭제 API")
    @WithMockUser
    void deleteFriendRequest() throws Exception {
        Long requestId = 1L;
        doNothing().when(friendManageService).deleteSentRequest(requestId);

        mockMvc.perform(delete("/api/v1/friends/request")
                        .param("requestId", requestId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(RestDocsTestUtils.expectCommonSuccess())
                .andDo(restDocs.document(
                        resource(ResourceSnippetParameters.builder()
                                .tag("친구 관련 API")
                                .description("보낸 친구 요청 삭제")
                                .responseFields(RestDocsFieldSnippets.COMMON_RESPONSE_FIELDS)
                                .build())
                ));
    }
}