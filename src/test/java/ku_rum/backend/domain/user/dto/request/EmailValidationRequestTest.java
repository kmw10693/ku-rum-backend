package ku_rum.backend.domain.user.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import ku_rum.backend.domain.common.mail.dto.request.EmailValidationRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;

class EmailValidationRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("이메일에 빈 문자열이 들어온 경우 처리한다.")
    void blankEmail(String email) {
        EmailValidationRequest loginIdValidationRequest = new EmailValidationRequest(email);

        assertThat(validator.validate(loginIdValidationRequest))
                .anyMatch(violation -> violation.getMessage().equals("이메일 입력은 필수입니다."));
    }
}