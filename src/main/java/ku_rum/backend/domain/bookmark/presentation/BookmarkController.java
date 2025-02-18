package ku_rum.backend.domain.bookmark.presentation;

import jakarta.validation.Valid;
import ku_rum.backend.domain.bookmark.application.BookmarkService;
import ku_rum.backend.domain.bookmark.dto.request.BookmarkSaveRequest;
import ku_rum.backend.domain.notice.dto.response.NoticeSimpleResponse;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.response.BaseResponse;
import ku_rum.backend.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.BOOKMARK_SUCCESS;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Validated
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/save")
    public BaseResponse<String> addBookmark(@RequestBody @Valid final BookmarkSaveRequest bookmarkRequest) {
        bookmarkService.addBookmark(bookmarkRequest);
        return BaseResponse.ok(BOOKMARK_SUCCESS.getMessage());
    }

    @GetMapping("/find")
    public BaseResponse<List<NoticeSimpleResponse>> getBookmarks() {
        return BaseResponse.ok(bookmarkService.getBookmarks());
    }
}
