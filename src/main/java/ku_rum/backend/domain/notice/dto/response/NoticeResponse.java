package ku_rum.backend.domain.notice.dto.response;


import ku_rum.backend.domain.notice.domain.Notice;

public record NoticeResponse(
        Long id,
        Integer categoryId,
        String categoryName,
        String title,
        String link,
        String pubDate,
        String author,
        String description
) {
    public static NoticeResponse from(Notice n) {
        return new NoticeResponse(
                n.getId(),
                n.getCategoryId(),
                n.getCategoryName(),
                n.getTitle(),
                n.getLink(),
                n.getPubDate(),
                n.getAuthor(),
                n.getDescription()
        );
    }
}

