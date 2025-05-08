package ku_rum.backend.domain.category.presentation;

import ku_rum.backend.domain.category.application.CategoryViewService;
import ku_rum.backend.domain.category.dto.response.CategoryViewPlaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryViewController {
    private final CategoryViewService categoryViewService;

    @GetMapping("/{categoryName}/places")
    public List<CategoryViewPlaceResponse> getPlacesByCategoryName(@PathVariable String categoryName) {
        return categoryViewService.getPlacesByCategoryName(categoryName);
    }
}
