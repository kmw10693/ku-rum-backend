package ku_rum.backend.domain.building.service;

import ku_rum.backend.domain.building.application.BuildingSearchService;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.domain.category.dto.response.CategoryDetailResponse;
import ku_rum.backend.global.exception.building.BuildingNotFoundException;
import ku_rum.backend.global.exception.category.CategoryNotExistException;
import ku_rum.backend.global.exception.category.CategoryNotProvidingDetailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ku_rum.backend.global.support.response.status.BaseExceptionResponseStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
            assertEquals(36, buildingResponses.size());
        }


        @DisplayName("건물번호로 건물정보를 출력")
        @Test
        public void viewBuildingByNumber_success() throws Exception {
            // given
            Long number = 3L;
            BuildingResponse buildingResponse = buildingSearchService.viewBuildingByNumber(number);

            //when then
            assertEquals("상허연구관", buildingResponse.buildingName());
        }

        @DisplayName("건물이름으로 건물정보를 출력")
        @Test
        public void viewBuildingByName_success() throws Exception {
            // given
            String name = "공학관";
            BuildingResponse buildingResponse = buildingSearchService.viewBuildingByName(name);

            //when then
            assertEquals("공학관", buildingResponse.buildingName());
        }

        @DisplayName("카테고리 이름으로 카테고리용 건물정보를 출력")
        @Test
        public void viewBuildingByCategory_success() throws Exception {
            // given
            String name = "레스티오_공대점";
            BuildingResponse buildingResponse = buildingSearchService.viewBuildingByName(name);

            //when then
            assertEquals("레스티오_공학관점", buildingResponse.buildingName());
        }

        @DisplayName("full text로 검색어 결과 출력")
        @Test
        public void viewAvailableTextNameList_success() throws Exception{
            //given
            String text = "cu";
            List<BuildingResponse> buildingResponseList = buildingSearchService.searchAvailableText(text);

            //when then
            assertAll(
                    () -> assertEquals(3, buildingResponseList.size()),
                    () -> assertEquals( buildingResponseList.stream()
                            .allMatch(building ->
                                    building.buildingAbbreviation().toLowerCase()
                                            .contains("cu".toLowerCase())),
                            true),
                    () -> assertEquals( buildingResponseList.stream()
                            .allMatch(building ->
                                    building.buildingName().toLowerCase()
                                            .contains("cu".toLowerCase())),
                            true)
            );
        }

        @DisplayName("디테일 반환하는 카테고리의 디테일 정보 출력")
        @Test
        public void viewBuildingByCategoryInBuilding_success() throws Exception{
            //given
            Long buildingId = 36L;
            String text = "학생 식당";
            CategoryDetailResponse categoryDetailResponse = buildingSearchService.viewBuildingDetailByCategory(text, buildingId);

            //when then
            assertAll(
                    () -> assertEquals(9, categoryDetailResponse.detailList().size()),
                    () -> assertEquals( categoryDetailResponse.category(),text)
            );
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

        @DisplayName("full text로 검색어 결과 출력 - 실패")
        @Test
        public void viewAvailableTextNameList_failure() throws Exception{
            //given
            String text = "1111";

            //when then
            assertThatThrownBy(() ->
                    buildingSearchService.searchAvailableText(text))
                    .isInstanceOf(BuildingNotFoundException.class)
                    .hasMessageContaining(BUILDING_DATA_NOT_FOUND_BY_NAME.getMessage());


        }

        @DisplayName("디테일 반환하는 카테고리의 디테일 정보 출력 - 실패")
        @Test
        public void viewBuildingByCategoryInBuilding_failure() throws Exception{
            //given
            Long buildingId = 34L;
            String text = "레레레";

            //when then
            assertThatThrownBy(() ->
                    buildingSearchService.viewBuildingDetailByCategory(text, buildingId))
                    .isInstanceOf(CategoryNotProvidingDetailException.class)
                    .hasMessageContaining(CATEGORYNAME_NOT_PROVIDING_DETAIL.getMessage());
        }
}
}