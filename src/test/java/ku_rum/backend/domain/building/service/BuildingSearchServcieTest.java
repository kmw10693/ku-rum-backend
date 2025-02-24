package ku_rum.backend.domain.building.service;

import ku_rum.backend.domain.building.application.BuildingSearchService;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.global.exception.building.BuildingNotFoundException;
import ku_rum.backend.global.exception.category.CategoryNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WebAppConfiguration
@SpringBootTest
@Transactional
public class BuildingSearchServcieTest {

    @Autowired
    BuildingSearchService buildingSearchService;

    @DisplayName("성공")
    @Nested
    class Success{
        @DisplayName("학교의 모든 건물정보를 출력")
        @Test
        public void viewAll_success() throws Exception {
            // given
            List<BuildingResponse> buildingResponses = buildingSearchService.findAllBuildings();

            //when then
            Assertions.assertEquals(26, buildingResponses.size());
        }


        @DisplayName("건물번호로 건물정보를 출력")
        @Test
        public void viewBuildingByNumber_success() throws Exception {
            // given
            Long number = 3L;
            BuildingResponse buildingResponse = buildingSearchService.viewBuildingByNumber(number);

            //when then
            Assertions.assertEquals("상허연구관", buildingResponse.buildingName());
        }

        @DisplayName("건물이름으로 건물정보를 출력")
        @Test
        public void viewBuildingByName_success() throws Exception {
            // given
            String name = "공학관";
            BuildingResponse buildingResponse = buildingSearchService.viewBuildingByName(name);

            //when then
            Assertions.assertEquals("공학관", buildingResponse.buildingName());
        }

        @DisplayName("카테고리 이름으로 카테고리용 건물정보를 출력")
        @Test
        public void viewBuildingByCategory_success() throws Exception {
            // given
            String name = "레스티오_공대점";
            BuildingResponse buildingResponse = buildingSearchService.viewBuildingByName(name);

            //when then
            Assertions.assertEquals("레스티오_공학관점", buildingResponse.buildingName());
        }
    }

    @DisplayName("실패")
    @Nested
    class Failure{
        @DisplayName("존재하지 않는 건물번호로 입력으로 건물정보 출력 실패")
        @Test
        public void viewBuildingByNumber_failure() throws Exception {
            // given
            Long number = 100L;

            //when then
            assertThatThrownBy(() -> buildingSearchService.viewBuildingByNumber(number))
                    .isInstanceOf(BuildingNotFoundException.class)
                    .hasMessageContaining(BUILDING_DATA_NOT_FOUND_BY_NUMBER.getMessage());
        }

        @DisplayName("존재하지 않는 건물이름으로 건물정보를 출력 실패")
        @Test
        public void viewBuildingByName_failure() throws Exception {
            // given
            String name = "투썸프레이스";

            //when then
            assertThatThrownBy(() -> buildingSearchService.viewBuildingByName(name))
                    .isInstanceOf(BuildingNotFoundException.class)
                    .hasMessageContaining(BUILDING_DATA_NOT_FOUND_BY_NAME.getMessage());
        }

        @DisplayName("존재하지 않는 카테고리 이름으로 건물정보를 출력 실패")
        @Test
        public void viewBuildingByCategory_failure() throws Exception {
            // given
            String name = "점점점";

            //when then
            assertThatThrownBy(() ->  buildingSearchService.viewBuildingByCategory(name))
                    .isInstanceOf(CategoryNotExistException.class)
                    .hasMessageContaining(CATEGORY_NAME_NOT_EXIST.getMessage());
        }
    }

    @DisplayName("등록된_건물정보_이름으로_조회_성공")
    @Test
    public void 등록된_건물정보_이름으로_조회_성공() throws Exception {
        // given
        BuildingResponse buildingResponse = buildingSearchService.viewBuildingByName("경영102");

        // then
        Assertions.assertEquals(2L, buildingResponse.buildingNumber());
    }

    @DisplayName("등록된_건물정보_건물번호로_조회_실패")
    @Test
    public void 등록된_건물정보_건물번호로_조회_실패() throws Exception {
        // given
        Long number = 202L;
        Assertions.assertThrows(BuildingNotFoundException.class, () -> {
            buildingSearchService.viewBuildingByNumber(number);
        });
    }

}