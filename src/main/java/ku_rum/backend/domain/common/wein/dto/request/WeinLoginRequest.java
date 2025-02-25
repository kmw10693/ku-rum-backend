package ku_rum.backend.domain.common.wein.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class WeinLoginRequest {
    @NotBlank(message = "사용자 아이디는 필수입니다.")
    private String userId;

    @NotBlank(message = "사용자 비밀번호는 필수입니다.")
    private String password;

}