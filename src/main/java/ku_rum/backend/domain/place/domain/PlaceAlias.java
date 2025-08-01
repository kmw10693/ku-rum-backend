package ku_rum.backend.domain.place.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class PlaceAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeAliasId;

    @NotNull
    private String name;

    @NotNull
    private String replacement;
}
