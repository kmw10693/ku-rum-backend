package ku_rum.backend.domain.place.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.ion.Decimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long place_id;

    @ManyToOne
    private Category category;

    @NotNull
    private String name;

    @NotNull
    private String subName;

    @NotNull
    private String content;

    @NotNull
    private Decimal latitude;

    @NotNull
    private Decimal longitude;
}
