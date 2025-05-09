package ku_rum.backend.domain.category.application;

import ku_rum.backend.domain.category.dto.response.CategoryViewPlaceResponse;
import ku_rum.backend.domain.place.domain.repository.PlaceViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryViewService {

    private final PlaceViewRepository placeViewRepository;

    public List<CategoryViewPlaceResponse> getPlacesByCategoryName(String categoryName) {
        return placeViewRepository.findAllByCategory_Name(categoryName).stream()
                .map(CategoryViewPlaceResponse::from)
                .toList();
    }
}
