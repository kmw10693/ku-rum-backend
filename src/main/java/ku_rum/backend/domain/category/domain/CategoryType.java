package ku_rum.backend.domain.category.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    STUDENT_CAFETERIA("학생 식당"),
    KCUBE("케이큐브"),
    KHUB("케이허브"),
    CU("씨유편의점"),
    RESTIO("레스티오"),
    CAFE_1894("1894카페");

    private final String text;
}
