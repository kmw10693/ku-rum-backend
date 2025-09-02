package ku_rum.backend.integration.domain.place.config;

import ku_rum.backend.domain.place.domain.repository.PlaceAliasRepository;
import ku_rum.backend.domain.place.domain.repository.PlaceRepository;
import ku_rum.backend.integration.domain.place.data.PlaceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PlaceTestConfig {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    PlaceAliasRepository placeAliasRepository;

    @Bean
    public PlaceData placeData() {
        return new PlaceData(placeRepository, placeAliasRepository);
    }
}