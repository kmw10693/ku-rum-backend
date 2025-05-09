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
                BigDecimal.valueOf(37.544520),
                BigDecimal.valueOf(127.076300),
                savedBuildings.get(1),
                savedCategories.get(1)
        ));
        places.add(Place.of(
                "케이큐브-공학관점",
                BigDecimal.valueOf(37.544520),
                BigDecimal.valueOf(127.076280),
                savedBuildings.get(1),
                savedCategories.get(1)
        ));

        /**
         * 카테고리 : 케이허브
         */


        /**
         * 카테고리 : 편의점
         */

        /**
         * 카테고리 : 레스티오
         */

        /**
         * 카테고리 : 1984
         */

        /**
         * 카테고리 : 학생식당
         */

        /**
         * 카테고리 : 학과사무실
         */

        /**
         * 카테고리 : 기숙사
         */

        /**
         * 카테고리 : 은행
         */

        /**
         * 카테고리 : 우체국
         */
        return places;
    }
}
