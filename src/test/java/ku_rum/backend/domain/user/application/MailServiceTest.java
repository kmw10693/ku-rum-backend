package ku_rum.backend.domain.user.application;

import ku_rum.backend.domain.common.mail.application.MailService;
import ku_rum.backend.domain.common.mail.dto.request.MailSendRequest;
import ku_rum.backend.domain.common.mail.dto.request.MailVerificationRequest;
import ku_rum.backend.domain.common.mail.dto.response.MailVerificationResponse;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.global.exception.user.DuplicateEmailException;
import ku_rum.backend.global.exception.user.MailSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("인증을 보낸 이메일이 중복인 경우 예외 처리한다.")
    void DuplicateEmailCheck() {
        // given
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> mailService.sendCodeToEmail(new MailSendRequest(email)))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("인증을 보낸 이메일이 중복이 아닌 경우 정상 작동된다.")
    void NonDuplicateEmailCheck() {
        // given
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        doThrow(new RuntimeException()).when(emailSender).send(any(SimpleMailMessage.class));

        // when & then
        assertThatThrownBy(() -> mailService.sendCodeToEmail(new MailSendRequest(email)))
                .isInstanceOf(MailSendException.class);
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 - 성공")
    void codeVerifySuccess() {
        // given
        String email = "test@example.com";
        String authCode = "123456";
        String key = "auth_code:" + email;

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(authCode);
        when(redisTemplate.hasKey(any())).thenReturn(true);

        mailService.sendCodeToEmail(new MailSendRequest(email));

        // when
        MailVerificationResponse response = mailService.verifiedCode(new MailVerificationRequest(email, authCode));

        // then
        assertThat(response.isVerified()).isTrue();
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 - 실패")
    void codeVerifyFailure() {
        // given
        String email = "test@example.com";
        String wrongAuthCode = "654321";
        String key = "auth_code:" + email;

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn("123456");
        when(redisTemplate.hasKey(any())).thenReturn(true);

        // when
        MailVerificationResponse response = mailService.verifiedCode(new MailVerificationRequest(email, wrongAuthCode));

        // then
        assertThat(response.isVerified()).isFalse();
    }
}
