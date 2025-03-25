package ku_rum.backend.global.batch;

import ku_rum.backend.domain.notice.application.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.HashMap;

import static ku_rum.backend.domain.notice.application.NoticeRedisKey.NOTICE_REDIS_KEY_PREFIX;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SetRedisKeyStep {
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    @JobScope
    public Step setRedisKey(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("setRedisKey", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(setKeyToRedis(), platformTransactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet setKeyToRedis() {
        return (contribution, chunkContext) -> {
            log.info("Redis Key 저장 시작");

            ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
            ArrayList<String> crawlingUrls = (ArrayList<String>) jobExecutionContext.get("crawlingUrls");

            if (crawlingUrls == null || crawlingUrls.isEmpty()) {
                log.info("Redis에 저장할 공지사항이 없음");
                return RepeatStatus.FINISHED;
            }

            for (String key : crawlingUrls) {
                redisTemplate.opsForValue().set(NOTICE_REDIS_KEY_PREFIX.getPrefix() + key, key);
                log.info("Redis 저장 -> Key: {}, Value: {}", NOTICE_REDIS_KEY_PREFIX.getPrefix() + key, key);
            }

            return RepeatStatus.FINISHED;
        };
    }
}
