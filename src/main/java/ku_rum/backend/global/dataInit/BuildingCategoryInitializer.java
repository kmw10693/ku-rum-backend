package ku_rum.backend.global.dataInit;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.category.domain.BuildingCategory;
import ku_rum.backend.domain.category.domain.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BuildingCategoryInitializer {
  public static ArrayList<BuildingCategory> initialize(ArrayList<Building> buildings, ArrayList<Category> categories) {
    ArrayList<BuildingCategory> buildingCategories = new ArrayList<>();

    //빌딩
    Building 동물생명과학관 = buildings.get(12-1);
    Building 공학관 = buildings.get(21-1);
    Building 제1학생회관 = buildings.get(20-1);
    Building 상허도서관 = buildings.get(9-1);
    Building 생명과학관 = buildings.get(11-1);
    Building 상허연구관 = buildings.get(3-1);
    Building 건축관 = buildings.get(17-1);
    Building 경영관 = buildings.get(2-1);
    Building 과학관 = buildings.get(23-1);
    Building 새천년관 = buildings.get(16-1);
    

    //카테고리
    Category 레스티오 = categories.get(2-1);
    Category 카페_1984 = categories.get(1-1);
    Category cu = categories.get(3-1);
    Category KCUBE = categories.get(4-1);
    Category KHUB = categories.get(5-1);
    Category 학생식당 = categories.get(6-1);

    //레스티오 추가
    buildingCategories.add(
            BuildingCategory.of(동물생명과학관, 레스티오)
    );
    buildingCategories.add(
            BuildingCategory.of(공학관, 레스티오)
    );

    //카페_1984 추가
    buildingCategories.add(
            BuildingCategory.of(제1학생회관, 카페_1984)
    );
    buildingCategories.add(
            BuildingCategory.of(상허도서관, 카페_1984)
    );

    //cu 추가
    buildingCategories.add(
          BuildingCategory.of(제1학생회관, cu)
    );
    buildingCategories.add(
            BuildingCategory.of(상허도서관, cu)
    );

    //KCUBE 추가
    buildingCategories.add(
          BuildingCategory.of(생명과학관,KCUBE)      
    );
    buildingCategories.add(
            BuildingCategory.of(상허도서관,KCUBE)
    );
    buildingCategories.add(
            BuildingCategory.of(동물생명과학관,KCUBE)
    );
    buildingCategories.add(
            BuildingCategory.of(생명과학관,KCUBE)
    );
    buildingCategories.add(
            BuildingCategory.of(공학관,KCUBE)
    );
    buildingCategories.add(
            BuildingCategory.of(상허연구관,KCUBE)
    );

    //KHUB 추가
    buildingCategories.add(
            BuildingCategory.of(건축관,KHUB)
    );
    buildingCategories.add(
            BuildingCategory.of(경영관,KHUB)
    );
    buildingCategories.add(
            BuildingCategory.of(과학관,KHUB)
    );

    //학생식당 추가
    buildingCategories.add(
            BuildingCategory.of(제1학생회관,학생식당)
    );
    buildingCategories.add(
            BuildingCategory.of(상허도서관,학생식당)
    );
    buildingCategories.add(
            BuildingCategory.of(새천년관,학생식당)
    );


    return buildingCategories;


  }
}