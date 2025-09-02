package ku_rum.backend.domain.place.domain;

import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
@Table(indexes = {@Index(name = "original_index", columnList = "original")})
public class PlaceAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeAliasId;

    @NotNull
    private String original;

    @NotNull
    private String replacement;
}