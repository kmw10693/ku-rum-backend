package ku_rum.backend.Integration.domain.place.data;

import java.math.BigDecimal;
import ku_rum.backend.domain.place.domain.CategoryChip;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.domain.SubPlace;
import ku_rum.backend.domain.place.domain.repository.PlaceRepository;
import ku_rum.backend.domain.place.domain.repository.SubPlaceRepository;
import org.springframework.transaction.annotation.Transactional;

public class PlaceData {

    PlaceRepository placeRepository;
    SubPlaceRepository subPlaceRepository;

    public PlaceData(PlaceRepository placeRepository, SubPlaceRepository subPlaceRepository) {
        this.placeRepository = placeRepository;
        this.subPlaceRepository = subPlaceRepository;
    }

    @Transactional
    public void savePlaceData() {
        Place place1 = Place.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("공학관")
                .subName("공학관입니다.")
                .abbreviation("공학관 A,B,C,D동이 있습니다.")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place1);

        Place place2 = Place.builder()
                .categoryChip(CategoryChip.K_CUBE)
                .name("공학관 케이큐브")
                .subName("공학관 케이큐브입니다.")
                .abbreviation("건국대학교 위인전에 예약 후 사용할 수 있습니다.")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place2);

        Place place3 = Place.builder()
                .categoryChip(CategoryChip.DEPARTMENT_OFFICE)
                .name("컴퓨터공학부")
                .subName("공과대학 소속")
                .abbreviation("학과 사무실은 공학관 1층에 위치합니다")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place3);

        Place place4 = Place.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("경영관")
                .subName("경영대학 건물")
                .abbreviation("후문 좌측에 위치입니다")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place4);

        Place place5 = Place.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("상허연구관")
                .subName("기술 경영, ICT")
                .abbreviation("경영관 뒤에 위치합니다")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place5);

        Place place6 = Place.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("교육과학관")
                .subName("이과대학건물")
                .abbreviation("공학관, 창의관 사이에 위치합니다")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place6);

        SubPlace subPlace1 = SubPlace.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("공학관A")
                .subName("공학관 중앙에 위치한 공학관A동입니다.")
                .abbreviation("공학관 중앙 건물입니다")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .place(place1)
                .build();
        subPlaceRepository.save(subPlace1);

        SubPlace subPlace2 = SubPlace.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("공학관B")
                .subName("공학관 좌측에 위치한 공학관B동입니다.")
                .abbreviation("공학관 건물, 중장비 연구실 맞은편에 있습니다.")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .place(place1)
                .build();
        subPlaceRepository.save(subPlace2);

        SubPlace subPlace3 = SubPlace.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("공학관C")
                .subName("공학관 우측에 위치한 공학관C동입니다.")
                .abbreviation("신공학관 맞은 편에 있습니다.")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .place(place1)
                .build();
        subPlaceRepository.save(subPlace3);

        SubPlace subPlace4 = SubPlace.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("공학관D")
                .subName("공학관 건물 안에 있는 공학관D동입니다.")
                .abbreviation("공학관 ㄷ자 안에 있습니다.")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .place(place1)
                .build();
        subPlaceRepository.save(subPlace4);
    }

    @Transactional
    public void afterEach() {
        subPlaceRepository.deleteAll();
        placeRepository.deleteAll();
    }
}
