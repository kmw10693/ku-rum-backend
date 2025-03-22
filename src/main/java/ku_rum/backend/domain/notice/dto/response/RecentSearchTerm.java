package ku_rum.backend.domain.notice.dto.response;

import java.util.List;

public record RecentSearchTerm(
        Long userId,
        List<String> searchedList
) {

    public static RecentSearchTerm of(Long userId, List<String> searchedList) {
        return new RecentSearchTerm(userId, searchedList);
    }
}
