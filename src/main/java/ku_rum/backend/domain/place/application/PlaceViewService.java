package ku_rum.backend.domain.place.application;

import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.repository.BuildingViewRepository;
import ku_rum.backend.domain.building.dto.response.BuildingViewResponse;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.domain.repository.PlaceViewRepository;
import ku_rum.backend.domain.place.dto.request.LocationRequest;
import ku_rum.backend.domain.place.dto.response.LocationInfoResponse;
import ku_rum.backend.domain.place.dto.response.PlaceDetailView;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.exception.place.PlaceNotFoundException;
import ku_rum.backend.global.utill.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.PLACE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlaceViewService {
    private final PlaceViewRepository placeViewRepository;
    private final BuildingViewRepository buildingViewRepository;
    private final UserUtil userUtil;

    public PlaceDetailView getPlaceDetail(Long placeId) {
        Place place = placeViewRepository.findPlaceById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(PLACE_NOT_FOUND));

        return new PlaceDetailView(
                place.getId(),
                place.getName(),
                place.getSubName(),
                place.getText(),
                place.getLatitude(),
                place.getLongitude(),
                place.getBuilding().getId(),
                place.getBuilding().getName(),
                place.getCategory().getId(),
                place.getCategory().getName()
        );
    }

    public LocationInfoResponse getUserLocation(LocationRequest locationRequest) {
        User user = userUtil.getUser();

        BigDecimal roundedLat = locationRequest.latitude().setScale(4, RoundingMode.HALF_UP);
        BigDecimal roundedLon = locationRequest.longtitude().setScale(4, RoundingMode.HALF_UP);


        // DB에서 모든 건물 정보 조회
        List<Building> buildings = buildingViewRepository.findAll();

        // 가장 가까운 건물 찾기
        Building nearestBuilding = buildings.stream()
                .min(Comparator.comparingDouble(
                        building -> haversineDistance(
                                roundedLat.doubleValue(), roundedLon.doubleValue(),
                                building.getLatitude().doubleValue(), building.getLongitude().doubleValue())))
                .orElseThrow(() -> new PlaceNotFoundException(PLACE_NOT_FOUND));

        //TODO: 사용자 공유 상태 조회 로직 추가 예정(현재는 isShare = true로 반환)


        return new LocationInfoResponse(
                true,
                BuildingViewResponse.from(
                        nearestBuilding
                )
        );

    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
