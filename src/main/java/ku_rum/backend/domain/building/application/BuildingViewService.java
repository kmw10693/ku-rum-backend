package ku_rum.backend.domain.building.application;


import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.dto.response.BuildingViewResponse;
import ku_rum.backend.domain.building.domain.repository.BuildingViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuildingViewService {

    private final BuildingViewRepository buildingRepository;

    public BuildingViewResponse getBuildingByNumber(Integer number) {
        Building building = buildingRepository.findByNumber(number)
                .orElseThrow(() -> new IllegalArgumentException("해당 번호의 건물이 존재하지 않습니다."));
        return BuildingViewResponse.from(building);
    }

    public List<BuildingViewResponse> searchBuildings(String name) {
        List<Building> buildings = buildingRepository.searchByNameOrAbbreviation(name);
        return buildings.stream()
                .map(BuildingViewResponse::from)
                .toList();
    }


}