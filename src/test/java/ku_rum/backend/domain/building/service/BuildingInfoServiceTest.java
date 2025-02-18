package ku_rum.backend.domain.building.service;

import jakarta.persistence.EntityManager;
import ku_rum.backend.domain.building.application.BuildingSearchService;
import ku_rum.backend.domain.building.domain.repository.BuildingQueryRepository;
import ku_rum.backend.domain.building.domain.repository.BuildingRepository;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.global.exception.building.BuildingNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@WebAppConfiguration
@SpringBootTest
@Transactional
public class BuildingInfoServiceTest {

    @Autowired
    BuildingSearchService buildingSearchService;

    @DisplayName("등록된_건물정보_전체_조회_성공")
    @Test
    public void 등록된_건물정보_전체_조회_성공() throws Exception {
        // given
        List<BuildingResponse> buildingResponses = buildingSearchService.findAllBuildings();

        //then
        Assertions.assertEquals(19, buildingResponses.size());
    }

    @DisplayName("등록된_건물정보_이름으로_조회_성공")
    @Test
    public void 등록된_건물정보_이름으로_조회_성공() throws Exception {
        // given
        BuildingResponse buildingResponse = buildingSearchService.viewBuildingByName("경영102").get();

        // then
        Assertions.assertEquals(2L, buildingResponse.buildingNumber());
    }

    @DisplayName("등록된_건물정보_건물번호로_조회_성공")
    @Test
    public void 등록된_건물정보_건물번호로_조회_성공() throws Exception {
        // given
        Long number = 3L;
        BuildingResponse buildingResponse = buildingSearchService.viewBuildingByNumber(number);

        // then
        Assertions.assertEquals("상허연구관", buildingResponse.buildingName());
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