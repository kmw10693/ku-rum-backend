package ku_rum.backend.domain.notice.application;

import ku_rum.backend.domain.notice.domain.Notice;
import ku_rum.backend.domain.notice.domain.NoticeCategory;
import ku_rum.backend.domain.notice.domain.NoticeStatus;
import ku_rum.backend.domain.notice.domain.repository.NoticeRepository;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private RedisTemplate<String, String> recentSearchRedisTemplate; // @Mock으로 변경


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("카테고리별 공지사항 조회 성공")
    @Test
    void findNoticesByCategorySuccess() {
        // given
        Notice notice = Notice.builder()
                .title("Notice Example")
                .url("https://konkuk.ac.kr")
                .date("2024-11-07")
                .noticeCategory(NoticeCategory.AFFAIR)
                .noticeStatus(NoticeStatus.GENERAL)
                .build();

        when(noticeRepository.findByNoticeCategory(NoticeCategory.AFFAIR))
                .thenReturn(List.of(notice));

        // when
        List<NoticeSimpleResponse> result = noticeService.findNoticesByCategory("학사");

        // then
        assertEquals(1, result.size());
        assertEquals("Notice Example", result.get(0).getTitle());
    }

    @DisplayName("검색어를 통한 공지사항 제목 검색 성공")
    @Test
    void searchNoticesByTitleSuccess() {
        // given
        Notice notice = Notice.builder()
                .title("Notice Search Example")
                .url("https://konkuk.ac.kr")
                .date("2024-11-07")
                .noticeCategory(NoticeCategory.AFFAIR)
                .noticeStatus(NoticeStatus.GENERAL)
                .build();

        // mock noticeRepository의 동작 설정
        when(noticeRepository.searchNoticesByTitle("Search"))
                .thenReturn(List.of(notice));

        // recentSearchRedisTemplate의 동작을 mock 처리
        ListOperations<String, String> mockListOperations = mock(ListOperations.class);
        when(recentSearchRedisTemplate.opsForList()).thenReturn(mockListOperations);

        // when
        List<NoticeSimpleResponse> result = noticeService.searchNoticesByTitle(1L, "Search");

        // then
        assertEquals(1, result.size());
        assertEquals("Notice Search Example", result.get(0).getTitle());
    }

}
