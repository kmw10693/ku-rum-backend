package ku_rum.backend.global.log.application;

import jakarta.servlet.http.HttpServletRequest;
import ku_rum.backend.global.log.domain.ApiLog;
import ku_rum.backend.global.log.domain.repository.ApiLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class AopLogging {

    private final ApiLogRepository apiLogRepository;

    @Autowired
    public AopLogging(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Around("execution(public * ku_rum.backend.domain.*.presentation.*.*(..)) || execution(public * ku_rum.backend.global.*.presentation.*.*(..))")
    @Transactional
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        var httpRequest = requestAttributes.getRequest();

        ApiLog log = apiLogRepository.save(new ApiLog(
                InetAddress.getLocalHost().getHostAddress(),
                httpRequest.getRequestURL().toString(),
                httpRequest.getMethod(),
                getClientIp(httpRequest),
                getRequestString(joinPoint)
        ));

        try {
            ResponseEntity<?> response = (ResponseEntity<?>) joinPoint.proceed();
            apiLogRepository.updateResponse(log.getSeq(), response.getStatusCodeValue(), response.getBody().toString());
            return response;
        } catch (Exception e) {
            apiLogRepository.updateResponse(log.getSeq(), exceptionToStatus(e).value(), e.getMessage() != null ? e.getMessage() : "error");
            throw e;
        }
    }

    private String getRequestString(ProceedingJoinPoint joinPoint) {
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();
        return Arrays.stream(parameterNames)
                .map(name -> name + "=" + parameterValues[Arrays.asList(parameterNames).indexOf(name)])
                .collect(Collectors.joining(", "));
    }

    private String getClientIp(HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getHeader("X-FORWARDED-FOR");
        if (clientIp == null || clientIp.isBlank()) {
            clientIp = httpRequest.getRemoteAddr();
        }
        return clientIp;
    }

    private static HttpStatus exceptionToStatus(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}