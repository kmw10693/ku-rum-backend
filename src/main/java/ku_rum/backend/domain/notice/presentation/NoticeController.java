package ku_rum.backend.domain.notice.presentation;

import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.domain.NoticeCategory;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ku_rum.backend.domain.notice.dto.response.CrawlingResponse.*;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 카테고리별 공지사항 조회 (페이징 처리)
     * @param category, page(page는 1부터)
     * @return
     */
    @GetMapping
    public BaseResponse<List<NoticeSimpleResponse>> getNoticesByCategory(@RequestParam(name = "category") NoticeCategory category,
                                                                         @RequestParam(name = "page") int page) {
        return BaseResponse.ok(noticeService.findNoticesByCategory(category, page));
    }

    /**
     * 공지사항 제목을 통한 검색 (페이징 처리)
     * @param searchTerm, page(page는 1부터)
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<NoticeSimpleResponse>> searchNotices(@RequestParam(name = "searchTerm") String searchTerm,
                                                                  @RequestParam(name = "page") int page) {
        return BaseResponse.ok(noticeService.searchNoticesByTitle(searchTerm, page));
    }
}
