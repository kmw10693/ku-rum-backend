package ku_rum.backend.domain.common.mail.application;

import ku_rum.backend.domain.common.mail.domain.MailAuth;
import ku_rum.backend.domain.common.mail.domain.repository.MailAuthRepository;
import ku_rum.backend.domain.common.mail.dto.request.MailSendRequest;
import ku_rum.backend.domain.common.mail.dto.request.MailVerificationRequest;
import ku_rum.backend.global.exception.user.MailSendException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private MailAuthRepository mailAuthRepository;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("이메일 인증 코드 전송 - 성공")
    void sendCodeToEmail_success() {
        // given
        MailSendRequest request = new MailSendRequest("test@example.com");
        MailAuth mailAuth = MailAuth.create(request.email(), 6);

        // when
        mailService.sendCodeToEmail(request);

        // then
        verify(mailSenderService, times(1)).send(eq(request.email()), anyString(), anyString());
        verify(mailAuthRepository, times(1)).save(any(MailAuth.class), any(Duration.class));
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 - 성공")
    void verifyCode_success() {
        // given
        String email = "test@example.com";
        String validCode = "123456";
        MailVerificationRequest request = new MailVerificationRequest(email, validCode);
        MailAuth mailAuth = MailAuth.of(email, validCode);

        given(mailAuthRepository.findByEmail(email)).willReturn(Optional.of(mailAuth));

        // when & then
        assertDoesNotThrow(() -> mailService.verifyCode(request));
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 - 실패 (잘못된 코드)")
    void verifyCode_invalidCode_fail() {
        // given
        String email = "test@example.com";
        String invalidCode = "999999";
        MailVerificationRequest request = new MailVerificationRequest(email, invalidCode);
        MailAuth mailAuth = MailAuth.of(email, "123456");

        given(mailAuthRepository.findByEmail(email)).willReturn(Optional.of(mailAuth));

        // when & then
        assertThrows(MailSendException.class, () -> mailService.verifyCode(request));
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 - 실패 (인증 코드 없음)")
    void verifyCode_notFound_fail() {
        // given
        String email = "test@example.com";
        MailVerificationRequest request = new MailVerificationRequest(email, "123456");

        given(mailAuthRepository.findByEmail(email)).willReturn(Optional.empty());

        // when & then
        assertThrows(MailSendException.class, () -> mailService.verifyCode(request));
    }
}
