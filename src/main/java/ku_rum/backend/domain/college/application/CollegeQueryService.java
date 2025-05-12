package ku_rum.backend.domain.college.application;

import ku_rum.backend.domain.college.domain.College;
import ku_rum.backend.domain.college.domain.repository.CollegeRepository;
import ku_rum.backend.domain.college.dto.response.CollegeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollegeQueryService {
    private final CollegeRepository collegeRepository;

    public CollegeResponse findAll() {
        List<College> all = collegeRepository.findAll();

        List<String> names = all.stream()
                .map(College::getName)
                .toList();

        return new CollegeResponse(names);
    }
}
