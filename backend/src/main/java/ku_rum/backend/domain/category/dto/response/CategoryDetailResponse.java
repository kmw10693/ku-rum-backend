package ku_rum.backend.domain.category.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryDetailResponse<T> (
        String category,
        Long floor,
        List<T> detailList
){
}