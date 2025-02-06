package ku_rum.backend.domain.category.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.Optional;

@Builder
public record CategoryDetailResponse<T> (
        String category,
        Long floor,
        Optional<List<T>> detailList
){
}