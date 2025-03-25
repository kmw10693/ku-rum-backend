package ku_rum.backend.domain.notice.domain.repository;

import ku_rum.backend.domain.notice.domain.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface NoticeRepositoryCustom {

    List<Notice> searchNoticesByTitleWithPaging(String searchTerm, int page, int pageSize);
}