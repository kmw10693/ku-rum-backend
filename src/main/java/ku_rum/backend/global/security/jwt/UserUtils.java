package ku_rum.backend.global.security.jwt;

import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.NO_SUCH_USER;


@RequiredArgsConstructor
@Component
public class UserUtils {

    private final UserRepository userRepository;

    public static Long getLongMemberId() {
        CustomUserDetails userDetails = getCustomUserDetails();
        return userDetails.getUserId();
    }

    private static CustomUserDetails getCustomUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
