package ku_rum.backend.domain.place.domain;

import lombok.Getter;

@Getter
public enum Category {
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
    POST_OFFICE("우체국");

    private final String name;

    Category(String name) {
        this.name = name;
    }
}
