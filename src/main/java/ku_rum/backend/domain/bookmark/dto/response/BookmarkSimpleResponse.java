package ku_rum.backend.domain.bookmark.dto.response;

import ku_rum.backend.domain.bookmark.domain.Bookmark;

import java.time.LocalDateTime;

public record BookmarkSimpleResponse(
        Long bookmarkId,
        String noticeTitle,
        LocalDateTime createdAt
) {
    public static BookmarkSimpleResponse from(Bookmark bookmark) {
        return new BookmarkSimpleResponse(
                bookmark.getId(),
                bookmark.getNotice().getTitle(),
                bookmark.getCreatedAt()
        );
    }
}
