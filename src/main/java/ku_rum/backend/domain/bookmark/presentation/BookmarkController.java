package ku_rum.backend.domain.bookmark.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.bookmark.application.BookmarkService;
import ku_rum.backend.domain.bookmark.dto.request.BookmarkSaveRequest;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.BOOKMARK_SUCCESS;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Validated
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * 사용자의 북마크 새로 저장
     *
     * @param bookmarkRequest
     * @return
     */
    @PostMapping("/save")
    public BaseResponse<String> addBookmark(@RequestBody @Valid final BookmarkSaveRequest bookmarkRequest) {
        bookmarkService.addBookmark(bookmarkRequest);
        return BaseResponse.ok(BOOKMARK_SUCCESS.getMessage());
    }

    /**
     * 사용자의 모든 북마크 불러오기
     *
     * @return
     */
    @GetMapping("/find")
    public BaseResponse<List<NoticeSimpleResponse>> getBookmarks() {
        return BaseResponse.ok(bookmarkService.getBookmarks());
    }

}
