package ku_rum.backend.global.config;

import ku_rum.backend.global.log.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor())
                .order(1) // (1)
                .addPathPatterns("/**") // (2)
                .excludePathPatterns("/error"); // (3)
    }
}
