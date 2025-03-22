package ku_rum.backend.domain.common.mail.application;

import ku_rum.backend.domain.common.mail.domain.MailAuth;
import ku_rum.backend.domain.common.mail.domain.repository.MailAuthRepository;
import ku_rum.backend.domain.common.mail.dto.request.MailSendRequest;
import ku_rum.backend.domain.common.mail.dto.request.MailVerificationRequest;
import ku_rum.backend.domain.common.mail.dto.response.MailVerificationResponse;
import ku_rum.backend.global.exception.user.MailSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static ku_rum.backend.domain.common.mail.domain.MailSendSetting.MAIL_SEND_INFO;
import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.INVALID_AUTH_CODE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailService {
    private final MailSenderService mailSenderService;
    private final MailAuthRepository mailAuthRepository;

    @Async
    @Transactional
    public void sendCodeToEmail(final MailSendRequest request) {
        deleteByEmail(request);

        MailAuth mailAuth = MailAuth.create(
                request.email(),
                MAIL_SEND_INFO.getCODE_LENGTH());

        mailSenderService.send(request.email(), MAIL_SEND_INFO.getTITLE(), mailAuth.getAuthCode());
        mailAuthRepository.save(mailAuth, Duration.ofMillis(MAIL_SEND_INFO.getAUTH_EXPIRED_MILLS()));
    }

    private void deleteByEmail(MailSendRequest request) {
        mailAuthRepository.findByEmail(request.email())
                .ifPresent(existingMailAuth -> {
                    mailAuthRepository.deleteByEmail(request.email());
                });
    }

    public void verifyCode(final MailVerificationRequest request) {
        MailAuth mailAuth = mailAuthRepository.findByEmail(request.email())
                .orElseThrow(() -> new MailSendException(INVALID_AUTH_CODE));

        mailAuth.isValid(request.code());
    }
}