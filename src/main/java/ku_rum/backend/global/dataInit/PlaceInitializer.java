package ku_rum.backend.global.dataInit;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.category.domain.Category;
import ku_rum.backend.domain.place.domain.Place;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class PlaceInitializer {
    public static ArrayList<Place> initialize(List<Building> savedBuildings, List<Category> savedCategories) {
        ArrayList<Place> places = new ArrayList<>();

        /**
         * 카테고리 : 단과대
         */
        places.add(Place.of("경영대학", BigDecimal.valueOf(37.544419), BigDecimal.valueOf(127.076370), savedBuildings.get(1), savedCategories.get(1)));
        places.add(Place.of("상허생명과학대학", BigDecimal.valueOf(37.544292), BigDecimal.valueOf(127.076212), savedBuildings.get(2), savedCategories.get(1)));
        places.add(Place.of("문과대학", BigDecimal.valueOf(37.542422), BigDecimal.valueOf(127.078929), savedBuildings.get(18), savedCategories.get(1)));
        places.add(Place.of("이과대학", BigDecimal.valueOf(37.541646), BigDecimal.valueOf(127.080408), savedBuildings.get(22), savedCategories.get(1)));
        places.add(Place.of("공과대학", BigDecimal.valueOf(37.541822), BigDecimal.valueOf(127.078845), savedBuildings.get(20), savedCategories.get(1)));
        places.add(Place.of("건축대학", BigDecimal.valueOf(37.543391), BigDecimal.valueOf(127.078521), savedBuildings.get(16), savedCategories.get(1)));
        places.add(Place.of("사범대학", BigDecimal.valueOf(37.544073), BigDecimal.valueOf(127.073994), savedBuildings.get(3), savedCategories.get(1)));
        places.add(Place.of("생명과학대학", BigDecimal.valueOf(37.541004), BigDecimal.valueOf(127.074197), savedBuildings.get(10), savedCategories.get(1)));
        places.add(Place.of("의과대학", BigDecimal.valueOf(37.541443), BigDecimal.valueOf(127.072313), savedBuildings.get(9), savedCategories.get(1)));
        places.add(Place.of("예술디자인대학", BigDecimal.valueOf(37.544419), BigDecimal.valueOf(127.073994), savedBuildings.get(4), savedCategories.get(1)));

        /**
         * 카테고리 : 케이큐브
         */
        places.add(Place.of(
                "케이큐브-경영관점",
                BigDecimal.valueOf(37.5444261),
                BigDecimal.valueOf(127.076408),
                savedBuildings.get(0),
                savedCategories.get(2)
        ));
        places.add(Place.of(
                "케이큐브-공학관점",
                BigDecimal.valueOf(37.5415912),
                BigDecimal.valueOf(127.0788622),
                savedBuildings.get(10),
                savedCategories.get(2)
        ));
        places.add(Place.of(
                "케이큐브-상허도서관점",
                BigDecimal.valueOf(37.5420942),
                BigDecimal.valueOf(127.073758),
                savedBuildings.get(8),
                savedCategories.get(2)
        ));

        /**
         * 카테고리 : 케이허브
         */


        /**
         * 카테고리 : 편의점
         */
        places.add(Place.of(
                "CU-제1학생회관점",
                BigDecimal.valueOf(37.5418177),
                BigDecimal.valueOf(127.0782949),
                savedBuildings.get(19),
                savedCategories.get(4)
        ));
        places.add(Place.of(
                "CU-경영관점",
                BigDecimal.valueOf(37.5443834),
                BigDecimal.valueOf(127.076221),
                savedBuildings.get(0),
                savedCategories.get(4)
        ));
        places.add(Place.of(
                "CU-상허기념도서관점",
                BigDecimal.valueOf(37.5420658),
                BigDecimal.valueOf(127.0740457),
                savedBuildings.get(8),
                savedCategories.get(4)
        ));

        /**
         * 카테고리 : 레스티오
         */
        places.add(Place.of(
                "레스티오-공학관점",
                BigDecimal.valueOf(37.5416511),
                BigDecimal.valueOf(127.0787021),
                savedBuildings.get(10),
                savedCategories.get(5)
        ));
        places.add(Place.of(
                "레스티오-경영관점",
                BigDecimal.valueOf(37.5444759),
                BigDecimal.valueOf(127.0765134),
                savedBuildings.get(0),
                savedCategories.get(5)
        ));
        places.add(Place.of(
                "레스티오-동물생명과학관점",
                BigDecimal.valueOf(37.5401054),
                BigDecimal.valueOf(127.0741121),
                savedBuildings.get(11),
                savedCategories.get(5)
        ));

        /**
         * 카테고리 : 1984
         */
        places.add(Place.of(
                "1984카페-제1학생회관점",
                BigDecimal.valueOf(37.5401054),
                BigDecimal.valueOf(127.0741121),
                savedBuildings.get(19),
                savedCategories.get(6)
        ));

        /**
         * 카테고리 : 학생식당
         */
        places.add(Place.of(
                "학생식당(구시아)-제1학생회관점",
                BigDecimal.valueOf(37.5419222),
                BigDecimal.valueOf(127.0779356),
                savedBuildings.get(19),
                savedCategories.get(7)
        ));
        places.add(Place.of(
                "학생식당(구시아)-상허기념도서관점",
                BigDecimal.valueOf(37.5419259),
                BigDecimal.valueOf(127.0737882),
                savedBuildings.get(8),
                savedCategories.get(7)
        ));

        /**
         * 카테고리 : 학과사무실
         */

        /**
         * 카테고리 : 기숙사
         */
        places.add(Place.of(
                "쿨하우스(기숙사)",
                BigDecimal.valueOf(37.5419259),
                BigDecimal.valueOf(127.0737882),
                savedBuildings.get(8),
                savedCategories.get(9)
        ));

        /**
         * 카테고리 : 은행
         */
        places.add(Place.of(
                "신한은행-건국대 제1학생회관점",
                BigDecimal.valueOf(37.5419876),
                BigDecimal.valueOf(127.0782326),
                savedBuildings.get(19),
                savedCategories.get(10)
        ));

        /**
         * 카테고리 : 우체국
         */
        places.add(Place.of(
                "우체국-건국대 제1학생회관점",
                BigDecimal.valueOf(37.5417271),
                BigDecimal.valueOf(127.0781763),
                savedBuildings.get(19),
                savedCategories.get(11)
        ));
        return places;
    }
}
