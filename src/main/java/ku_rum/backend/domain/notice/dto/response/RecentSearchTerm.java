package ku_rum.backend.domain.notice.dto.response;

import java.util.List;

public record RecentSearchTerm(
        Long userId,
        List<String> searchedList
) {

}
