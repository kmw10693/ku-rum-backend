package ku_rum.backend.domain.building.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.QBuilding;
import ku_rum.backend.domain.building.dto.response.BuildingResponse;
import ku_rum.backend.domain.category.domain.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BuildingQueryRepository {

  private final JPAQueryFactory queryFactory;
  private final QBuilding qBuilding = QBuilding.building;
  private final EntityManager entityManager;

  public List<BuildingResponse> findAllBuildings() {
    return queryFactory
            .select(Projections.constructor(BuildingResponse.class,
                    qBuilding.id,
                    qBuilding.name,
                    qBuilding.number,
                    qBuilding.abbreviation,
                    qBuilding.latitude,
                    qBuilding.longitude
            ))
            .from(qBuilding)
            .fetch();
  }

  public Optional<BuildingResponse> findBuildingByNumber(Long number) {
    return Optional.ofNullable(
            queryFactory
                    .select(Projections.constructor(BuildingResponse.class,
                            qBuilding.id,
                            qBuilding.name,
                            qBuilding.number,
                            qBuilding.abbreviation,
                            qBuilding.latitude,
                            qBuilding.longitude
                    ))
                    .from(qBuilding)
                    .where(qBuilding.number.eq(number))
                    .fetchOne()
    );
  }

  public Optional<BuildingResponse> findBuildingByName(String name) {
    return Optional.ofNullable(
            queryFactory
                    .select(Projections.constructor(BuildingResponse.class,
                            qBuilding.id,
                            qBuilding.name,
                            qBuilding.number,
                            qBuilding.abbreviation,
                            qBuilding.latitude,
                            qBuilding.longitude
                    ))
                    .from(qBuilding)
                    .where(qBuilding.name.eq(name))
                    .fetchOne()
    );
  }

  public List<Building> findAllByIdIn(List<Long> buildingIds) {
    return queryFactory
            .selectFrom(qBuilding)
            .where(qBuilding.id.in(buildingIds))
            .fetch();
  }

  public Optional<Building> findBuildingById(Long buildingId) {
    return Optional.ofNullable(
            queryFactory
                    .selectFrom(qBuilding)
                    .where(qBuilding.id.eq(buildingId))
                    .fetchOne()
    );
  }

  public Optional<Long> findBuildingIdByNumber(long number) {
    return Optional.ofNullable(
            queryFactory
                    .select(qBuilding.id)
                    .from(qBuilding)
                    .where(qBuilding.number.eq(number))
                    .fetchOne()
    );
  }

  public List<Building> findAllByCategory(Category category) {
    return queryFactory
            .selectFrom(qBuilding)
            .where(qBuilding.category.eq(category))
            .fetch();
  }

  public List<Building> searchBuildingByNgram(String searchText) {
    String nativeQuery = "SELECT * FROM building WHERE MATCH(name) AGAINST (?1 IN BOOLEAN MODE)";

    Query query = entityManager.createNativeQuery(nativeQuery, Building.class);
    query.setParameter(1, searchText + "*");

    return query.getResultList();
  }
}
