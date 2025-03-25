package ku_rum.backend.domain.notice.presentation;

import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.domain.NoticeCategory;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeViewController {

    private final NoticeService noticeService;

    /**
     * 공지사항 제목을 통한 검색 (페이징 처리)
     * @param searchTerm, page(page는 1부터)
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<NoticeSimpleResponse>> searchNotices(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @RequestParam(name = "searchTerm") String searchTerm,
                                                                  @RequestParam(name = "page") int page) {
        Long userId = userDetails.getUserId();
        return BaseResponse.ok(noticeService.searchNoticesByTitle(userId,searchTerm, page));
    }

    /**
     * 카테고리별 공지사항 조회 (페이징 처리)
     * @param category, page(page는 1부터)
     * @return
     */
    @GetMapping
    public BaseResponse<List<NoticeSimpleResponse>> getNoticesByCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @RequestParam(name = "category") NoticeCategory category,
                                                                         @RequestParam(name = "page") int page) {
        return BaseResponse.ok(noticeService.findNoticesByCategory(category, page));
    }



}