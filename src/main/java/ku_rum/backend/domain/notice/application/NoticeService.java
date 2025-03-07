package ku_rum.backend.domain.notice.application;

import ku_rum.backend.domain.notice.domain.Notice;
import ku_rum.backend.domain.notice.domain.NoticeCategory;
import ku_rum.backend.domain.notice.domain.NoticeStatus;
import ku_rum.backend.domain.notice.domain.repository.NoticeRepository;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Qualifier("urlRedisTemplate")
    private final RedisTemplate<String, String> urlRedisTemplate;

    /**
     * 카테고리별 공지사항 조회
     */
    public List<NoticeSimpleResponse> findNoticesByCategory(NoticeCategory category) {
        List<Notice> notices = noticeRepository.findByNoticeCategory(category);
        return notices.stream()
                .map(NoticeSimpleResponse::new)
                .toList();
    }

    /**
     * 제목으로 공지사항 검색
     */
    public List<NoticeSimpleResponse> searchNoticesByTitle(String searchTerm) {
        List<Notice> notices = noticeRepository.searchNoticesByTitle(searchTerm.trim());
        return notices.stream()
                .map(NoticeSimpleResponse::new)
                .toList();
    }


}
