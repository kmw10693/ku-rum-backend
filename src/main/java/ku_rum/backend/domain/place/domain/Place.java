package ku_rum.backend.domain.place.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeGroupId;

    @Enumerated(value = EnumType.STRING)
    private CategoryChip categoryChip;

    @NotNull
    private String name;

    private String subName;

    private String abbreviation;

    @NotNull
    private String content;

    @NotNull
    @Column(nullable = false, precision = 15, scale = 9)
    private BigDecimal latitude;

    @NotNull
    @Column(nullable = false, precision = 15, scale = 9)
    private BigDecimal longitude;
}
