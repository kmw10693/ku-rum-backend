package ku_rum.backend.domain.category.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {

    친구("친구"),
    단과대("단과대"),
    케이큐브("K-Cube"),
    케이허브("K-Hub"),
    편의점("편의점"),
    레스티오("레스티오"),
    카페1984("1984"),
    학생식당("학생식당"),
    학과사무실("학과사무실"),
    기숙사("기숙사"),
    은행("은행"),
    우체국("우체국");


    private final String text;

}