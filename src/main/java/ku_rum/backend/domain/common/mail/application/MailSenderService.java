package ku_rum.backend.domain.common.mail.application;

import ku_rum.backend.global.exception.user.MailSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.MAIL_SEND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender emailSender;

    public void send(String toEmail, String title, String text) {
        try {
            emailSender.send(createEmailForm(toEmail, title, text));
        } catch (RuntimeException e) {
            throw new MailSendException(MAIL_SEND_EXCEPTION);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        return message;
    }
}
