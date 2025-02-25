package ku_rum.backend.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Getter
public class OpenAiConfig {

    @Value("${openai.api.key}")
    private String openAiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiURL)
                .defaultHeader("Authorization", "Bearer " + openAiKey) // 인증 헤더 추가
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
