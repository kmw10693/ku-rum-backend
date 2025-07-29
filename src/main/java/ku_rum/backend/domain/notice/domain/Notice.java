package ku_rum.backend.domain.notice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer categoryId;

    private String categoryName;

    private String title;

    private String link;

    private String pubDate;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;
}
