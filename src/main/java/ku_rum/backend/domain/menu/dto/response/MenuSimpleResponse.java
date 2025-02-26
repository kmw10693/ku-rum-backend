package ku_rum.backend.domain.menu.dto.response;

import ku_rum.backend.domain.menu.domain.Menu;
import lombok.Builder;

@Builder
public record MenuSimpleResponse(
        String name,
        Long price,
        String imageUrl
) {
    public MenuSimpleResponse from(Menu menu) {
        return MenuSimpleResponse.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .imageUrl(menu.getImageUrl())
                .build();
    }


}
