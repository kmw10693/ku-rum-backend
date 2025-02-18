package ku_rum.backend.domain.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkSaveRequest {

    @NotNull(message = "url은 필수 입력값입니다.")
    private String url;

    @Builder
    public BookmarkSaveRequest(String url) {
        this.url = url;
    }

}
