package ku_rum.backend.domain.menu.response;

import ku_rum.backend.domain.menu.domain.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record MenuSimpleResponse (
        String  name,
        Long price,
        String imageUrl
){
  public MenuSimpleResponse from(Menu menu){
    return MenuSimpleResponse.builder()
            .name(menu.getName())
            .price(menu.getPrice())
            .imageUrl(menu.getImageUrl())
            .build();
  }


}
