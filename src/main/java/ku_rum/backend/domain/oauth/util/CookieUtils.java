package ku_rum.backend.domain.oauth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream)) {
            objectOutputStream.writeObject(object);
            return Base64.getUrlEncoder().encodeToString(byteStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to serialize object", e);
        }
    }

    public static <T> T deserialize(String str, Class<T> cls) {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.getUrlDecoder().decode(str)); ObjectInputStream objectInputStream = new ObjectInputStream(byteStream)) {
            Object obj = objectInputStream.readObject();
            return cls.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to deserialize object", e);
        }
    }

}
