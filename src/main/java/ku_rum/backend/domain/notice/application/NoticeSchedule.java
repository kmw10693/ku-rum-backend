/*
package ku_rum.backend.domain.notice.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeSchedule {

    private final ViewCountService viewCountService;
    private final NoticeService noticeService;

    @Scheduled(fixedRate = 90000) // 1.5분마다 실행 테스트
    public void syncViewCountsToDatabaseScheduled() {
        viewCountService.syncViewCountsToDatabase();
    }

}

*/
