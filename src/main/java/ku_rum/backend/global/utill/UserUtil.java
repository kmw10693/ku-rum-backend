package ku_rum.backend.global.utill;

import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import ku_rum.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.NO_SUCH_USER;

@RequiredArgsConstructor
@Component
public class UserUtil {

    private final UserRepository userRepository;

    public static Long getLongMemberId() {
        CustomUserDetails userDetails = getCustomUserDetails();
        return userDetails.getUserId();
    }

    public User getUser() {
        Long memberId = UserUtil.getLongMemberId();
        return userRepository.findUserById(memberId).orElseThrow
                (() -> new NoSuchUserException(NO_SUCH_USER));
    }

    private static CustomUserDetails getCustomUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
