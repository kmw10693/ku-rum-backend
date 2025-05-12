package ku_rum.backend.domain.college.presentation;

import ku_rum.backend.domain.college.application.CollegeQueryService;
import ku_rum.backend.domain.college.dto.response.CollegeResponse;
import ku_rum.backend.global.support.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/colleges")
@RequiredArgsConstructor
public class CollegeQueryController {

    private final CollegeQueryService collegeQueryService;

    @GetMapping
    public BaseResponse<CollegeResponse> getCollege() {
        return BaseResponse.ok(collegeQueryService.findAll());
    }
}
