package ku_rum.backend.global.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class LogUtility {

    @Getter
    @AllArgsConstructor
    public static class LogInfo {
        private String uuid;
        private String requestURI;
    }

    private static final ThreadLocal<LogInfo> REQUEST_INFO = new ThreadLocal<>(); // (1)

    public static void set(String uuid, String requestURI) {
        REQUEST_INFO.set(new LogInfo(uuid, requestURI));
    }

    public static String getUUID() {
        return REQUEST_INFO.get().getUuid();
    }

    public static String getRequestURI() {
        return REQUEST_INFO.get().getRequestURI();
    }

    public static void remove() {
        REQUEST_INFO.remove();
    }
}
