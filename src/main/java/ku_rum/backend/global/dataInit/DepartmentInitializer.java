package ku_rum.backend.global.dataInit;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.DepartmentType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DepartmentInitializer {
  public static ArrayList<Department> initializer(List<Building> buildings) {
    ArrayList<Department> departments = new ArrayList<>();

    // 빌딩
    Building 동물생명과학관 = buildings.get(12-1);
    Building 공학관 = buildings.get(21-1);
    Building 생명과학관 = buildings.get(11-1);
    Building 상허연구관 = buildings.get(3-1);
    Building 건축관 = buildings.get(17-1);
    Building 경영관 = buildings.get(2-1);
    Building 과학관 = buildings.get(23-1);
    Building 새천년관 = buildings.get(16-1);
    Building 인문학관 = buildings.get(19-1);

    // 각 건물별로 학과 추가
    addDepartmentsToBuilding(departments, 공학관,
            DepartmentType.컴퓨터공학부,
            DepartmentType.사회환경공학부,
            DepartmentType.기계공학부,
            DepartmentType.전기전자공학부,
            DepartmentType.화학공학부,
            DepartmentType.신산업융합학과,
            DepartmentType.K뷰티산업융합학과,
            DepartmentType.항공우주정보시스템공학과,
            DepartmentType.생물공학과,
            DepartmentType.산업공학과
    );

    addDepartmentsToBuilding(departments, 인문학관,
            DepartmentType.국어국문학과,
            DepartmentType.영어영문학과,
            DepartmentType.중어중문학과,
            DepartmentType.철학과,
            DepartmentType.사학과,
            DepartmentType.지리학과,
            DepartmentType.미디어커뮤니케이션학과,
            DepartmentType.문화콘텐츠학과,
            DepartmentType.휴먼ICT연계전공,
            DepartmentType.글로벌MICE연계전공,
            DepartmentType.인문상담치유연계전공,
            DepartmentType.통일인문교육연계전공,
            DepartmentType.정치외교학과,
            DepartmentType.경제학과,
            DepartmentType.행정학과,
            DepartmentType.국제무역학과,
            DepartmentType.응용통계학과,
            DepartmentType.융합인재학과,
            DepartmentType.글로벌비즈니스학과
    );

    addDepartmentsToBuilding(departments, 경영관,
            DepartmentType.경영학과,
            DepartmentType.기술경영학과,
            DepartmentType.부동산학과
    );

    addDepartmentsToBuilding(departments, 새천년관,
            DepartmentType.자유전공학부,
            DepartmentType.미래에너지공학과,
            DepartmentType.화장품공학과,
            DepartmentType.줄기세포재생공학과,
            DepartmentType.의생명공학과,
            DepartmentType.시스템생명공학과,
            DepartmentType.융합생명공학과
    );

    addDepartmentsToBuilding(departments, 생명과학관,
            DepartmentType.생명과학특성학과,
            DepartmentType.식량자원과학과,
            DepartmentType.식품유통공학과,
            DepartmentType.환경보건과학과,
            DepartmentType.산림조경학과
    );

    addDepartmentsToBuilding(departments, 동물생명과학관,
            DepartmentType.동물자원과학과,
            DepartmentType.축산식품생명공학과
    );

    return departments;
  }

  // 여러 학과를 한 번에 같은 건물에 추가하는 헬퍼 메소드
  private static void addDepartmentsToBuilding(ArrayList<Department> departments, Building building, DepartmentType... departmentTypes) {
    Arrays.stream(departmentTypes)
            .forEach(type -> departments.add(Department.of(type.name(), building)));
  }
}