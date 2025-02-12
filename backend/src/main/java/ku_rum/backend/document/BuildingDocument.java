package ku_rum.backend.document;

import jakarta.persistence.Id;
import ku_rum.backend.domain.building.domain.Building;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "posts", writeTypeHint = WriteTypeHint.FALSE)
@Setting(settingPath = "elastic/building-setting.json")
@Mapping(mappingPath = "elastic/building-mapping.json")
public class BuildingDocument {
  @Id
  private Long id;
  private String name;
  private String abbreviation;

  @Builder
  public BuildingDocument(Long id, String name, String abbreviation) {
    this.id = id;
    this.name = name;
    this.abbreviation = abbreviation;
  }

  public static BuildingDocument from(Building building){
    return BuildingDocument.builder()
            .id(building.getId())
            .name(building.getName())
            .abbreviation(building.getAbbreviation())
            .build();
  }

  public static List<BuildingDocument> from(List<Building> buildings) {
    return buildings.stream()
            .map(BuildingDocument::from)
            .collect(Collectors.toList());
  }
}
