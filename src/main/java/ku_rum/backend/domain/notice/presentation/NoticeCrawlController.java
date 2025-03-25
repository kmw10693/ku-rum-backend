package ku_rum.backend.domain.notice.presentation;

import ku_rum.backend.domain.notice.application.NoticeService;
import ku_rum.backend.global.security.CustomUserDetails;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ku_rum.backend.domain.notice.dto.response.CrawlingResponse.START_CRAWLING;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeCrawlController {

    private final NoticeService noticeService;

    /**
     * 주어진 조건(작성일 기준)을 만족하는 건국대학교 공지사항을 모두 크롤링
     * @return 성공메시지
     */
    @PostMapping("/crawl/konkuk")
    public BaseResponse<String> crawlKonkukNotices(@AuthenticationPrincipal CustomUserDetails userDetails) { //redis에 저장해두고 redis에 없으면 db에 저장을 한다 (key에다가 공지사항 url링크 저장)
        noticeService.crawlAndSaveKonkukNotices();
        return BaseResponse.ok(START_CRAWLING.getMessage());
    }


}