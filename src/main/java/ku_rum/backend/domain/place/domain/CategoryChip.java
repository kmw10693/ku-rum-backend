package ku_rum.backend.domain.place.domain;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum CategoryChip {
    BUILDING("건물"),
    COLLEGE("단과대"),
    K_CUBE("K-Cube"),
    K_HUB("K-Hub"),
    CONVENIENCE_STORE("편의점"),
    CAFE_RESTIO("레스티오"),
    CAFE_1847("1847"),
    STUDENT_CAFETERIA("학생식당"),
    DEPARTMENT_OFFICE("학과사무실"),
    DORMITORY("기숙사"),
    BANK("은행"),
    POST_OFFICE("우체국"),
    FRIEND("친구");

    private final String name;

    CategoryChip(String name) {
        this.name = name;
    }

    public static Optional<CategoryChip> from(String query) {
        return Arrays.stream(values())
                .filter(categoryChip -> categoryChip.getName().equals(query))
                .findFirst();
    }
}
