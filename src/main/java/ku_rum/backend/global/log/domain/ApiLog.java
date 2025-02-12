package ku_rum.backend.global.log.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String serverIp;

    @Column(length = 4096, nullable = false)
    private String requestUrl;

    @Column(nullable = false)
    private String requestMethod;

    private Integer responseStatus;

    @Column(nullable = false)
    private String clientIp;

    @Column(length = 4096, nullable = false)
    private String request;

    @Column(length = 4096)
    private String response;

    @Column(nullable = false)
    private LocalDateTime requestTime = LocalDateTime.now();

    private LocalDateTime responseTime;

    public ApiLog(String hostAddress, String requestUrl, String method, String clientIp, String requestString) {
        this.serverIp = hostAddress;
        this.requestUrl = requestUrl;
        this.requestMethod = method;
        this.clientIp = clientIp;
        this.request = requestString;
    }
}
