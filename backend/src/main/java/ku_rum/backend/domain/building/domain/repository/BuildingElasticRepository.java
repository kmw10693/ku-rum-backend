package ku_rum.backend.domain.building.domain.repository;

import ku_rum.backend.document.BuildingDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingElasticRepository extends ElasticsearchRepository<BuildingDocument, String> {

  /**
   * 건물 이름을 부분 검색 (와일드 카드 사용)
   *
   * @param name
   * @return
   */
  @Query("{\"wildcard\": {\"name\": {\"value\": \"*?0*\"}}}")
  List<BuildingDocument> findByNameCustom(String name);

}
