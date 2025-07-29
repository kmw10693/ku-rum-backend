package ku_rum.backend.domain.bookmark.application;

import ku_rum.backend.domain.bookmark.domain.Bookmark;
import ku_rum.backend.domain.bookmark.domain.repository.BookmarkRepository;
import ku_rum.backend.domain.bookmark.dto.response.BookmarkSimpleResponse;
import ku_rum.backend.domain.notice.domain.repository.NoticeRepository;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import ku_rum.backend.global.utill.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.NO_SUCH_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final UserUtil userUtil;

    private List<Bookmark> getBookmarksByUser(User user) {
        return bookmarkRepository.findByUser(user);
    }

    private User getUser() {
        Long memberId = UserUtil.getLongMemberId();
        return userRepository.findUserById(memberId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
    }

    public List<BookmarkSimpleResponse> getRecent5bookmarks() {
        User user = userUtil.getUser();
        List<Bookmark> recentBookmarks = bookmarkRepository.findTop5ByUserOrderByCreatedAtDesc(user);

        return recentBookmarks.stream()
                .map(BookmarkSimpleResponse::from)
                .collect(Collectors.toList());
    }
}
