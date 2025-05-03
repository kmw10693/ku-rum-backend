package ku_rum.backend.domain.notice.presentation;

import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.domain.notice.dto.response.RecentSearchTerm;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeRecentController {

    private final NoticeService noticeService;

    /**
     * 최근 검색어 목록 10개 가져오기
     * @param userDetails
     * @return
     */
    @GetMapping("/recent")
    public BaseResponse<RecentSearchTerm> searchTerms(@AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getUserId();
        return BaseResponse.ok(noticeService.getRecentSearchTerms(userId));
    }

    /**
     * 최근 공지사항 5개 반환
     * @return
     */
    @GetMapping("/recent/5notices")
    public BaseResponse<List<NoticeSimpleResponse>> recent5Notices(){
        return BaseResponse.ok(noticeService.getRecent5Notices());
    }
}