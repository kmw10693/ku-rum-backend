package ku_rum.backend.domain.notice.application;

import ku_rum.backend.domain.notice.domain.Notice;
import ku_rum.backend.domain.notice.domain.NoticeCategory;
import ku_rum.backend.domain.notice.domain.repository.NoticeRepository;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.global.exception.notice.InvalidPageException;
import ku_rum.backend.global.support.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final int PAGE_SIZE = 20;       //한 페이지에 들어갈 공지사항 개수 (추후에 논의)

    @Qualifier("urlRedisTemplate")
    private final RedisTemplate<String, String> urlRedisTemplate;

    /**
     * 카테고리별 공지사항 조회
     */
    //todo paging 예외처리
    public List<NoticeSimpleResponse> findNoticesByCategory(NoticeCategory category, int page) {

        validatePage(page);

        List<Notice> notices = noticeRepository.findByNoticeCategoryOrderByDateDesc(category, PageRequest.of(page - 1, PAGE_SIZE));
        return notices.stream()
                .map(NoticeSimpleResponse::new)
                .toList();
    }

    /**
     * 제목으로 공지사항 검색
     */
    //todo paging 예외처리
    public List<NoticeSimpleResponse> searchNoticesByTitle(String searchTerm, int page) {

        validatePage(page);

        List<Notice> notices = noticeRepository.searchNoticesByTitleWithPaging(searchTerm.trim(), page - 1, PAGE_SIZE);
        return notices.stream()
                .map(NoticeSimpleResponse::new)
                .toList();
    }

    private static void validatePage(int page) {
        //요청으로 들어온 page는 1부터 시작해야 함
        if (page < 1) {
            throw new InvalidPageException(INVALID_PAGE);
        }
    }

}
