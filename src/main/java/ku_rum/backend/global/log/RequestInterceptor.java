package ku_rum.backend.global.log;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uuid = UUID.randomUUID().toString(); // (1)
        String requestURI = request.getMethod() + " " + request.getRequestURL();

        log.info("Request [{}][{}]", uuid, requestURI); // (2)
        LogUtility.set(uuid, requestURI); // (3)

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        log.info("Response {} [{}][{}]", response.getStatus(), LogUtility.getUUID(), handler); // (4)
        LogUtility.remove(); // (5)
    }
}
