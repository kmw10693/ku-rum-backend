package ku_rum.backend.global.dataInit;

import ku_rum.backend.domain.college.domain.College;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CollegeInitializer {
    public static ArrayList<College> initialize() {
        ArrayList<College> colleges = new ArrayList<>();

        colleges.add(College.of("문과대학"));
        colleges.add(College.of("이과대학"));
        colleges.add(College.of("건축대학"));
        colleges.add(College.of("공과대학"));
        colleges.add(College.of("사회과학대학"));
        colleges.add(College.of("경영대학"));
        colleges.add(College.of("부동산과학원"));
        colleges.add(College.of("KU융합과학기술원"));
        colleges.add(College.of("상허생명과학대학"));
        colleges.add(College.of("수의과대학"));
        colleges.add(College.of("예술디자인대학"));
        colleges.add(College.of("사범대학"));
        colleges.add(College.of("상허교양대학"));
        colleges.add(College.of("KU혁신공유대학"));

        return colleges;
    }
}