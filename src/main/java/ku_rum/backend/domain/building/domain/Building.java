package ku_rum.backend.domain.building.domain;

import jakarta.persistence.Entity;
import ku_rum.backend.global.support.type.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building extends BaseEntity {

}
