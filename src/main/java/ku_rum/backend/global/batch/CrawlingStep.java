package ku_rum.backend.global.batch;

import ku_rum.backend.domain.notice.application.CrawlingService;
import ku_rum.backend.domain.notice.domain.Notice;
import ku_rum.backend.domain.notice.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CrawlingStep {

    private final NoticeRepository noticeRepository;
    private final CrawlingService crawlingService;

    private final int CHUNK_SIZE = 100;
    public final String JOB_EXECUTION_CONTEXT_VARIABLE = "crawlingUrls";

    @Bean
    @JobScope
    public Step crawling(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("crawlingStep", jobRepository)
                .<Notice, Notice>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(crawlingReader())
                .writer(crawlingWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Notice> crawlingReader() {

        List<Notice> notices = crawlingService.crawlAndSaveKonkukNotices();

        if (notices == null || notices.isEmpty()) {
            log.info("크롤링된 공지사항이 없습니다. Step을 종료합니다.");
            return new ListItemReader<>(Collections.emptyList()); // Step 종료
        }

        return new ListItemReader<>(notices);
    }

    @Bean
    @StepScope
    public ItemWriter<Notice> crawlingWriter() {
        return items -> {

            StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();

            ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();

            Set<String> urlSet = new HashSet<>();
            List<Notice> noticesWithoutDuplicateUrl = new ArrayList<>();

            for (Notice notice : items.getItems()) {
                if (!urlSet.contains(notice.getUrl())) {
                    noticesWithoutDuplicateUrl.add(notice);
                    urlSet.add(notice.getUrl());
                } else {
                    log.info("중복 제거된 공지사항: {}", notice.getUrl());
                }
            }

            List<Notice> uniquedNotices = noticesWithoutDuplicateUrl.stream()
                    .filter(notice -> !noticeRepository.existsByUrl(notice.getUrl()))
                    .toList();

            if (uniquedNotices.isEmpty()) {
                log.info("새로운 공지사항이 없습니다. 저장을 생략합니다.");
                return;
            }

            ArrayList<String> crawlingUrls = (ArrayList<String>) jobExecutionContext.get(JOB_EXECUTION_CONTEXT_VARIABLE);


            if (crawlingUrls == null || crawlingUrls.isEmpty()) {
                crawlingUrls = new ArrayList<>(urlSet);
                jobExecutionContext.put("crawlingUrls", crawlingUrls);
            } else {
                crawlingUrls.addAll(urlSet);
            }

            log.info("크롤링한 {}개의 공지사항 저장", uniquedNotices.size());

            // 크롤링한 데이터를 DB에도 저장
            noticeRepository.saveAll(uniquedNotices);
        };
    }

}
