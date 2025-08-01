package ku_rum.backend.Integration.domain.place.data;

import java.math.BigDecimal;
import ku_rum.backend.domain.place.domain.CategoryChip;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.domain.PlaceAlias;
import ku_rum.backend.domain.place.domain.SubPlace;
import ku_rum.backend.domain.place.domain.repository.PlaceAliasRepository;
import ku_rum.backend.domain.place.domain.repository.PlaceRepository;
import ku_rum.backend.domain.place.domain.repository.SubPlaceRepository;
import org.springframework.transaction.annotation.Transactional;

public class PlaceData {

    PlaceRepository placeRepository;
    SubPlaceRepository subPlaceRepository;
    PlaceAliasRepository placeAliasRepository;

    public PlaceData(PlaceRepository placeRepository, SubPlaceRepository subPlaceRepository,
                     PlaceAliasRepository placeAliasRepository) {
        this.placeRepository = placeRepository;
        this.subPlaceRepository = subPlaceRepository;
        this.placeAliasRepository = placeAliasRepository;
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

        PlaceAlias placeAlias1 = PlaceAlias.builder()
                .name("공B")
                .replacement("공학관B")
                .build();
        placeAliasRepository.save(placeAlias1);

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

        PlaceAlias placeAlias2 = PlaceAlias.builder()
                .name("경영")
                .replacement("경영관")
                .build();
        placeAliasRepository.save(placeAlias2);

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

        PlaceAlias placeAlias3 = PlaceAlias.builder()
                .name("상허관")
                .replacement("상허연구관")
                .build();
        placeAliasRepository.save(placeAlias3);

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

        PlaceAlias placeAlias4 = PlaceAlias.builder()
                .name("사")
                .replacement("교육과학관")
                .build();
        placeAliasRepository.save(placeAlias4);

        Place place7 = Place.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("예술문화관")
                .subName("예디대건물")
                .abbreviation("도서관 좌측에 위치합니다")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place7);

        Place place8 = Place.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("언어교육관")
                .subName("언어원")
                .abbreviation("도서관 3층 맞은 편에 위치합니다")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place8);

        PlaceAlias placeAlias5 = PlaceAlias.builder()
                .name("언어원")
                .replacement("언어교육관")
                .build();
        placeAliasRepository.save(placeAlias5);

        Place place9 = Place.builder()
                .categoryChip(CategoryChip.BUILDING)
                .name("법학관")
                .subName("종강, 법")
                .abbreviation("박물관 좌측에 위치합니다")
                .content("내용")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(12.12))
                .build();
        placeRepository.save(place9);

        PlaceAlias placeAlias6 = PlaceAlias.builder()
                .name("법")
                .replacement("법학관")
                .build();
        placeAliasRepository.save(placeAlias6);

        PlaceAlias placeAlias7 = PlaceAlias.builder()
                .name("종강")
                .replacement("법학관")
                .build();
        placeAliasRepository.save(placeAlias7);

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
        placeAliasRepository.deleteAll();
    }
}
