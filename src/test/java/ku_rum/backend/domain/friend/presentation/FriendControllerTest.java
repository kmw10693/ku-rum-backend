package ku_rum.backend.domain.friend.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.friend.application.FriendService;
import ku_rum.backend.domain.friend.dto.request.FriendFindRequest;
import ku_rum.backend.domain.friend.dto.request.FriendListRequest;
import ku_rum.backend.global.config.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static javax.management.openmbean.SimpleType.STRING;
import static javax.swing.text.html.parser.DTDConstants.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class FriendControllerTest  extends RestDocsTestSupport {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendService friendService;

    @DisplayName("유저의 친구 목록을 성공적으로 불러온다.")
    @Test
    void getFriendLists() throws Exception {
        //given
        FriendListRequest request = FriendListRequest.from(1L);

        // when then
        mockMvc.perform(get("/api/v1/friends")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("userId")
                                        .type(JsonType.NUMBER)
                                        .description("멤버 인덱스")
                                        .attributes(constraints("친구 목록을 가져올 사용자 인덱스"))
                        ),
                        responseFields(
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
                        )));
    }

    @DisplayName("기존 친구 목록에서 닉네임으로 친구를 검색한다.")
    @Test
    void findByNameInFriendLists() throws Exception {
        //given
        FriendFindRequest request = FriendFindRequest.of(1L, "nickname");

        // when then
        mockMvc.perform(get("/api/v1/friends/find")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("userId")
                                        .type(JsonType.NUMBER)
                                        .description("멤버 인덱스")
                                        .attributes(constraints("친구 목록을 가져올 사용자 인덱스")),
                                fieldWithPath("nickname")
                                        .type(JsonType.STRING)
                                        .description("멤버 인덱스")
                                        .attributes(constraints("찾고자 하는 친구 닉네임"))
                        ),
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonType.NUMBER)
                                        .description("성공시 반환 코드 (200)"),
                                fieldWithPath("status")
                                        .type(JsonType.STRING)
                                        .description("성공시 상태 값 (OK)"),
                                fieldWithPath("message")
                                        .type(JsonType.STRING)
                                        .description("성공 시 메시지 값 (OK)")
                        )));
    }

}