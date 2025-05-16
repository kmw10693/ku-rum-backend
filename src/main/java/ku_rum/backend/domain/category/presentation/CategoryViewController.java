package ku_rum.backend.domain.category.presentation;

import ku_rum.backend.domain.category.application.CategoryViewService;
import ku_rum.backend.domain.category.dto.response.CategoryViewPlaceResponse;
import ku_rum.backend.global.support.response.BaseResponse;
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

    /**
     * 카테고리 별로 해당 장소 모두 검색
     *
     * @param categoryName
     * @return
     */
    @GetMapping("/{categoryName}/places")
    public BaseResponse<List<CategoryViewPlaceResponse>> getPlacesByCategoryName(@PathVariable("categoryName") String categoryName) {
        return BaseResponse.ok(categoryViewService.getPlacesByCategoryName(categoryName));
    }


}
