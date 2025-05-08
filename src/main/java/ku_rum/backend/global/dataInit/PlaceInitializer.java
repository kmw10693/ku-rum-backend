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
        places.add(Place.of(
                "공과대학",
                BigDecimal.valueOf(37.543150),
                BigDecimal.valueOf(127.075120),
                savedBuildings.get(0),
                savedCategories.get(0)
        ));

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
