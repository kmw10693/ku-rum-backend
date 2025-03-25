package ku_rum.backend.domain.notice.presentation;

import ku_rum.backend.domain.notice.application.NoticeService;
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
     * 공지사항 제목을 통한 검색
     * @param searchTerm
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<NoticeSimpleResponse>> searchNotices(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(name = "searchTerm") String searchTerm) {
        Long userId = userDetails.getUserId();
        return BaseResponse.ok(noticeService.searchNoticesByTitle(userId,searchTerm));
    }

    /**
     * 카테고리별 공지사항 조회
     * @param category
     * @return
     */
    @GetMapping
    public BaseResponse<List<NoticeSimpleResponse>> getNoticesByCategory(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(name = "category") String category) {
        return BaseResponse.ok(noticeService.findNoticesByCategory(category));
    }



}