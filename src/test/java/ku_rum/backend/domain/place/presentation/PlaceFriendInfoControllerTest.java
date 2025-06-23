package ku_rum.backend.domain.place.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import ku_rum.backend.config.RestDocsTestSupport;
import ku_rum.backend.domain.friend.application.FriendReportService;
import ku_rum.backend.domain.friend.domain.repository.FriendBlockRepository;
import ku_rum.backend.domain.notice.domain.repository.NoticeRepositoryImpl;
import ku_rum.backend.domain.place.application.PlaceFriendInfoService;
import ku_rum.backend.domain.place.dto.request.DeleteSearchTermRequest;
import ku_rum.backend.domain.place.dto.request.PlaceSearchRequest;
import ku_rum.backend.domain.place.dto.response.PlaceFriendInfo2Response;
import ku_rum.backend.domain.place.dto.response.PlaceFriendInfoResponse;
import ku_rum.backend.domain.place.dto.response.PlaceSearchInfoResponse;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.domain.user.application.UserValidator;
import ku_rum.backend.global.batch.BatchScheduler;
import ku_rum.backend.global.domain.repository.ApiLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.JsonType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PlaceFriendInfoControllerTest extends RestDocsTestSupport {

    @MockBean
    private UserService userService;

    @MockBean
    private PlaceFriendInfoService placeFriendInfoService;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @MockBean
    private BatchScheduler batchScheduler;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserValidator userValidator;

    @MockBean
    private NoticeRepositoryImpl noticeRepository;

    @MockBean
    private ApiLogRepository apiLogRepository;

    @MockBean
    private FriendReportService friendManageService;

    @MockBean
    private FriendBlockRepository friendBlockRepository;


    @DisplayName("칩 이름으로 장소 정보와 해당 장소에 위치 공유 중인 친구 정보를 반환한다.")
    @Test
    //@WithMockUser
    void getChipDetailInfo() throws Exception {
        // given
        String chipName = "레스티오";

        List<PlaceFriendInfoResponse.Friend> friends1 = List.of(
                new PlaceFriendInfoResponse.Friend("남원홍", "https://s3-bucket-url.com/cat.jpg"),
                new PlaceFriendInfoResponse.Friend("김욱희", "https://s3-bucket-url.com/unknownss.jpg")
        );

        List<PlaceFriendInfoResponse> response = List.of(
                new PlaceFriendInfoResponse("레스티오 공학관점", "Restio", "라떼 4800원", BigDecimal.valueOf(26.4446), BigDecimal.valueOf(137.2325), List.of(
                        new PlaceFriendInfoResponse.Friend("김욱희", "https://url.com/profile.jpg")
                ))
        );


        given(placeFriendInfoService.getPlaceFriendInfoList(chipName))
                .willReturn(response);

        // when then
        mockMvc.perform(post("/api/v1/map/chip/{chipName}", chipName)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].mainTitle").value("레스티오 공학관점"))
                .andExpect(jsonPath("$.data[0].friendList[0].nickname").value("김욱희"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("5. 칩 이름으로 장소 정보 및 위치 공유 중인 친구 목록 조회")
                                .pathParameters(
                                        parameterWithName("chipName").description("칩 이름 (예: 레스티오)")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonType.STRING).description("응답 코드"),
                                        fieldWithPath("status").type(JsonType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].mainTitle").type(JsonType.STRING).description("장소 제목"),
                                        fieldWithPath("data[].subTitle").type(JsonType.STRING).description("장소 부제목"),
                                        fieldWithPath("data[].text").type(JsonType.STRING).description("장소 설명"),
                                        fieldWithPath("data[].latitude").type(JsonType.NUMBER).description("위도"),
                                        fieldWithPath("data[].longitude").type(JsonType.NUMBER).description("경도"),
                                        fieldWithPath("data[].friendList[].nickname").type(JsonType.STRING).description("위치 공유 중인 친구 닉네임"),
                                        fieldWithPath("data[].friendList[].profileUrl").type(JsonType.STRING).description("친구 프로필 이미지 URL")
                                )
                                .build()
                )));
    }

    @DisplayName("친구 칩 클릭 시, 위치 공유 중인 친구들의 장소 정보를 반환한다.")
    @Test
    @WithMockUser
    void getFriendsChipDetailInfo() throws Exception {
        // given
        List<PlaceFriendInfo2Response.Place> place1 = List.of(
                new PlaceFriendInfo2Response.Place("경영관", "경영", "경영관점 레스티오 아아 5000원", BigDecimal.valueOf(23.4446), BigDecimal.valueOf(138.2325))
        );

        List<PlaceFriendInfo2Response.Place> place2 = List.of(
                new PlaceFriendInfo2Response.Place("제2학생회관", "학관", "우체국 있음", BigDecimal.valueOf(23.4446), BigDecimal.valueOf(138.2325))
        );

        List<PlaceFriendInfo2Response> response = List.of(
                new PlaceFriendInfo2Response("남원홍", place1),
                new PlaceFriendInfo2Response("김욱희", place2)
                // 박소영은 범위 밖으로 나간 경우 응답에 포함되지 않음
        );

        given(placeFriendInfoService.getPlaceFriendInfoList2())
                .willReturn(response);

        // when then
        mockMvc.perform(post("/api/v1/map/chip/friends")
                        .header("Authorization", "Bearer ACCESS_TOKEN")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].nickname").value("남원홍"))
                .andExpect(jsonPath("$.data[0].place[0].mainTitle").value("경영관"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("6. 친구 칩 클릭 시 위치 공유 중인 친구들의 장소 정보 조회")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonType.STRING).description("응답 코드"),
                                        fieldWithPath("status").type(JsonType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].nickname").type(JsonType.STRING).description("친구 닉네임"),
                                        fieldWithPath("data[].place[].mainTitle").type(JsonType.STRING).description("장소 제목"),
                                        fieldWithPath("data[].place[].subTitle").type(JsonType.STRING).description("장소 부제목"),
                                        fieldWithPath("data[].place[].text").type(JsonType.STRING).description("장소 설명"),
                                        fieldWithPath("data[].place[].latitude").type(JsonType.NUMBER).description("위도"),
                                        fieldWithPath("data[].place[].longitude").type(JsonType.NUMBER).description("경도")
                                )
                                .build()
                )));
    }


    @DisplayName("장소 키워드 검색 시, 관련된 장소 제목 리스트를 반환한다.")
    @Test
    @WithMockUser
    void getPlacesNameBySearch() throws Exception {
        // given
        String search = "종강";
        PlaceSearchRequest request = new PlaceSearchRequest(search);

        List<PlaceSearchInfoResponse> response = List.of(
                new PlaceSearchInfoResponse("종합강의동 102호"),
                new PlaceSearchInfoResponse("종합강의동 103호")
        );

        given(placeFriendInfoService.getPlacesNameBySearch(search))
                .willReturn(response);

        // when then
        mockMvc.perform(post("/api/v1/map/search")
                        .header("Authorization", "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].mainTitle").value("종합강의동 102호"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("7. 건물명, 강의실명, 건물번호 검색 - 여러 장소 제목 반환")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .requestFields(
                                        fieldWithPath("search").type(JsonFieldType.STRING).description("검색할 키워드")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].mainTitle").type(JsonFieldType.STRING).description("장소 제목")
                                )
                                .build()
                )));
    }

    @DisplayName("건물명, 강의실명, 건물번호 검색 디테일 반환")
    @Test
    @WithMockUser
    void getDetailWithPlacesNameBySearch() throws Exception {
        // given
        String search = "종합강의동 102호";
        PlaceSearchRequest request = new PlaceSearchRequest(search);

        List<PlaceFriendInfoResponse.Friend> friendList = List.of(
                new PlaceFriendInfoResponse.Friend("남원홍", "https://s3-bucket-url.com/cat.jpg"),
                new PlaceFriendInfoResponse.Friend("김욱희", "https://s3-bucket-url.com/unknownss.jpg")
        );

        List<PlaceFriendInfoResponse> response = List.of(
                new PlaceFriendInfoResponse(
                        "종합강의동 102호",    // mainTitle
                        "종강 102",          // subTitle
                        "블라블라블라블라 어쩌구",  // text
                        BigDecimal.valueOf(23.4446), // latitude
                        BigDecimal.valueOf(138.2325), // longitude
                        friendList
                )
        );

        given(placeFriendInfoService.getDetailWithPlacesNameBySearch(search))
                .willReturn(response);

        // when then
        mockMvc.perform(post("/api/v1/map/search/detail")
                        .header("Authorization", "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].mainTitle").value("종합강의동 102호"))
                .andExpect(jsonPath("$.data[0].subTitle").value("종강 102"))
                .andExpect(jsonPath("$.data[0].text").value("블라블라블라블라 어쩌구"))
                .andExpect(jsonPath("$.data[0].latitude").value(23.4446))
                .andExpect(jsonPath("$.data[0].longitude").value(138.2325))
                .andExpect(jsonPath("$.data[0].friendList[0].nickname").value("남원홍"))
                .andExpect(jsonPath("$.data[0].friendList[0].profileUrl").value("https://s3-bucket-url.com/cat.jpg"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("8. 건물명, 강의실명, 건물번호 검색 - 디테일 정보 반환")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .requestFields(
                                        fieldWithPath("search").type(JsonFieldType.STRING).description("검색할 키워드")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].mainTitle").type(JsonFieldType.STRING).description("장소 제목"),
                                        fieldWithPath("data[].subTitle").type(JsonFieldType.STRING).description("장소 부제목"),
                                        fieldWithPath("data[].text").type(JsonFieldType.STRING).description("장소 설명"),
                                        fieldWithPath("data[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                        fieldWithPath("data[].longitude").type(JsonFieldType.NUMBER).description("경도"),
                                        fieldWithPath("data[].friendList[].nickname").type(JsonFieldType.STRING).description("위치 공유 중인 친구 닉네임"),
                                        fieldWithPath("data[].friendList[].profileUrl").type(JsonFieldType.STRING).description("친구 프로필 이미지 URL")
                                )
                                .build()
                )));
    }


    @DisplayName("건물명, 강의실명, 건물번호 검색어 리스트 조회")
    @Test
    @WithMockUser
    void getSearchTermList() throws Exception {
        // given
        List<String> searchTerms = List.of("신공학관", "종강102", "레스티오");
        given(placeFriendInfoService.getSearchTermList()).willReturn(searchTerms);

        // when & then
        mockMvc.perform(get("/api/v1/map/search/term")
                        .header("Authorization", "Bearer ACCESS_TOKEN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0]").value("신공학관"))
                .andExpect(jsonPath("$.data[1]").value("종강102"))
                .andExpect(jsonPath("$.data[2]").value("레스티오"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("9. 건물명, 강의실명, 건물번호 검색어 리스트 반환")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("최근 검색어 리스트")
                                )
                                .build()
                )));
    }


    @DisplayName("건물명, 강의실명, 건물번호 검색어 삭제")
    @Test
    @WithMockUser
    void deleteSearchTerm() throws Exception {
        // given
        String term = "신공학관";
        DeleteSearchTermRequest request = new DeleteSearchTermRequest(term);

        given(placeFriendInfoService.deleteSearchTerm(term)).willReturn("최근 검색어 삭제 완료");

        // when & then
        mockMvc.perform(delete("/api/v1/map/search/term")
                        .header("Authorization", "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value("최근 검색어 삭제 완료"))
                .andDo(restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("NEW 지도 API")
                                .description("10. 건물명, 강의실명, 건물번호 검색어 삭제")
                                .requestHeaders(
                                        headerWithName("Authorization").description("액세스 토큰 (Bearer Token)")
                                )
                                .requestFields(
                                        fieldWithPath("term").type(JsonFieldType.STRING).description("삭제할 검색어")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("삭제 결과 메시지")
                                )
                                .build()
                )));
    }




}
