package ku_rum.backend.domain.building.domain.repository;

import ku_rum.backend.document.BuildingDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingElasticRepository extends ElasticsearchRepository<BuildingDocument, Long> {

  @Query("{\"wildcard\": {\"name\": {\"value\": \"*?0*\"}}}")
  List<BuildingDocument> findByNameCustom(String name);
}
