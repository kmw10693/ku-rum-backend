package ku_rum.backend.domain.friend.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.friend.application.FriendService;
import ku_rum.backend.domain.friend.dto.request.FriendFindRequest;
import ku_rum.backend.domain.friend.dto.request.FriendListRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
class FriendControllerTest  extends RestDocsTestSupport {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendService friendService;

    @DisplayName("유저의 친구 목록을 성공적으로 불러온다.")
    @Test
    @WithMockUser
    void getFriendLists() throws Exception {
        //given
        FriendListRequest request = FriendListRequest.from(1L);

        // when then
        mockMvc.perform(get("/api/v1/friends")
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("친구 API")
                                        .description("유저의 친구 목록 조회")
                                        .requestHeaders(
                                headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
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
                                        .description("데이터 성공시 빈 값")
                        ).build())));
    }

    @DisplayName("기존 친구 목록에서 닉네임으로 친구를 검색한다.")
    @Test
    @WithMockUser
    void findByNameInFriendLists() throws Exception {
        //given
        FriendFindRequest request = FriendFindRequest.from("nickname");

        // when then
        mockMvc.perform(get("/api/v1/friends/find?nickname=nickname")
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("친구 API")
                                        .description("닉네임으로 친구를 검색")
                                        .queryParameters(parameterWithName("nickname").description("찾을 닉네임입니다."))
                                        .requestHeaders(
                                headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
                        )
                                        .requestFields(
                                fieldWithPath("nickname")
                                        .type(JsonType.STRING)
                                        .description("멤버 인덱스")
                                        .attributes(constraints("찾고자 하는 친구 닉네임"))
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
                                        .description("성공 시 메시지 값 (OK)")
                        ).build())));
    }

    @DisplayName("친구 요청을 보낸다.")
    @Test
    @WithMockUser
    void requestFriends() throws Exception {
        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/friends/{requestId}", 1)
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("친구 요청이 성공적으로 완료되었습니다."))

                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("친구 API")
                                        .description("친구 요청")

                                        .pathParameters( // 5
                                parameterWithName("requestId").description("요청할 ID입니다.")
                        )
                                        .requestHeaders(
                                headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
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
                                        .description("성공 시 데이터 값 (OK)")
                        ).build())));
    }

    @DisplayName("친구 요청을 승인한다.")
    @Test
    @WithMockUser
    void accept() throws Exception {
        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/friends/{requestId}/accept", 1)
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("친구 요청이 성공적으로 완료되었습니다."))

                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("친구 API")
                                        .description("친구 승인")
                                        .pathParameters( // 5
                                parameterWithName("requestId").description("요청할 ID입니다.")
                        )
                                        .requestHeaders(
                                headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
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
                                        .description("성공 시 데이터 값 (OK)")
                        ).build())));
    }

    @DisplayName("친구 요청을 거절한다.")
    @Test
    @WithMockUser
    void deny() throws Exception {
        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/friends/{requestId}/deny", 1)
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("친구 거절 되었습니다."))

                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("친구 API")
                                        .description("친구 요청 거절")
                                        .pathParameters(
                                parameterWithName("requestId").description("요청할 ID입니다.")
                        )
                                        .requestHeaders(
                                headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
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
                                        .description("성공 시 데이터 값 (OK)")
                        ).build())));
    }

    @DisplayName("친구를 삭제한다.")
    @Test
    @WithMockUser
    void delete() throws Exception {
        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/friends/{requestId}", 1)
                        .header("Bearer", "6ce1d11af9ac1adf97712c069ca33bc4564d675ce3958942bb3dc5601829881430cfd8d98c8745")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("친구 삭제 되었습니다."))

                .andDo(restDocs.document(
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("친구 API")
                                        .description("친구 삭제")
                                        .pathParameters( // 5
                                parameterWithName("requestId").description("요청할 ID입니다.")
                        )
                                        .requestHeaders(
                                headerWithName("Bearer").description("발급 받은 엑세스 토큰입니다.")
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
                                        .description("성공 시 데이터 값 (OK)")
                        ).build())));
    }

}