package ku_rum.backend.domain.category.presentation;

import ku_rum.backend.domain.category.application.CategoryViewService;
import ku_rum.backend.domain.place.domain.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryViewController {
    private final CategoryViewService categoryViewService;

    @GetMapping("/{categoryName}/places")
    public List<Place> getPlacesByCategoryName(@PathVariable String categoryName) {
        return categoryViewService.getPlacesByCategoryName(categoryName);
    }
}
