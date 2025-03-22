package ku_rum.backend.domain.notice.presentation;

import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.domain.notice.dto.response.RecentSearchTerm;
import ku_rum.backend.domain.user.application.UserService;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ku_rum.backend.global.utils.UserUtils.getLongMemberId;

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
    @GetMapping("/")
    public BaseResponse<RecentSearchTerm> searchTerms(@AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getUserId();
        return BaseResponse.ok(noticeService.getRecentSearchTerms(userId));
    }
}
