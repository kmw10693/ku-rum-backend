package ku_rum.backend.Integration.domain.place.config;

import ku_rum.backend.Integration.domain.place.data.PlaceData;
import ku_rum.backend.domain.place.domain.repository.PlaceAliasRepository;
import ku_rum.backend.domain.place.domain.repository.PlaceRepository;
import ku_rum.backend.domain.place.domain.repository.SubPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PlaceTestConfig {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    SubPlaceRepository subPlaceRepository;

    @Autowired
    PlaceAliasRepository placeAliasRepository;

    @Bean
    public PlaceData placeData() {
        return new PlaceData(placeRepository, subPlaceRepository, placeAliasRepository);
    }
}
