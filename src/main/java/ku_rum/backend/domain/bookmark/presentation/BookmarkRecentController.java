package ku_rum.backend.domain.bookmark.presentation;

import ku_rum.backend.domain.bookmark.application.BookmarkService;
import ku_rum.backend.domain.bookmark.dto.response.BookmarkSimpleResponse;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkRecentController {

    private final BookmarkService bookmarkService;

    /**
     * 최근 저장한 북마크 5개 반환
     * @return
     */
    @GetMapping("/recent/5bookmarks")
    public BaseResponse<List<BookmarkSimpleResponse>> recent5bookmakrs(){
        return BaseResponse.ok(bookmarkService.getRecent5bookmarks());
    }
}
