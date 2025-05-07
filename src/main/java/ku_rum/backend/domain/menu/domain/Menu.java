package ku_rum.backend.domain.menu.domain;

import jakarta.persistence.*;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    private Long price;

    @Column(length = 300)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Builder
    private Menu(String name, Long price, String imageUrl, Place place) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.place = place;
    }

    public static Menu of(String name, Long price, String imageUrl, Place place) {
        return Menu.builder()
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .place(place)
                .build();
    }
}
