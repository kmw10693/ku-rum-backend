package ku_rum.backend.domain.place.application;

import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.domain.repository.PlaceViewRepository;
import ku_rum.backend.domain.place.dto.response.PlaceDetailView;
import ku_rum.backend.global.exception.place.PlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.PLACE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlaceViewService {
    private final PlaceViewRepository placeViewRepository;

    public PlaceDetailView getPlaceDetail(Long placeId) {
        Place place = placeViewRepository.findById(placeId)
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
}
