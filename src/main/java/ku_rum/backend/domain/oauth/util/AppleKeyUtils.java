package ku_rum.backend.domain.oauth.util;

import ku_rum.backend.global.exception.global.GlobalException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.OAUTH_INVALID_PRIVATE_KEY;

public class AppleKeyUtils {

    public static PrivateKey loadPrivateKey(String location) {
        try {
            Resource resource;
            if (location.startsWith("classpath:")) {
                resource = new ClassPathResource(location.substring("classpath:".length()));
            } else {
                resource = new FileSystemResource(location);
            }

            try (InputStream is = resource.getInputStream()) {
                String key = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s+", "");

                byte[] decoded = Base64.getDecoder().decode(key);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
                KeyFactory keyFactory = KeyFactory.getInstance("EC"); // ES256 = EC
                return keyFactory.generatePrivate(keySpec);
            }
        } catch (Exception e) {
            throw new GlobalException(OAUTH_INVALID_PRIVATE_KEY);
        }
    }
}
