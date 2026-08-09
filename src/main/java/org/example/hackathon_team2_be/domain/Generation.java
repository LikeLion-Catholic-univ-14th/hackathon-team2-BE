package org.example.hackathon_team2_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "generations")
@Getter
@NoArgsConstructor
public class Generation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "archive_product_id", nullable = false)
    private Long archiveProductId;

    @Column(name = "future_context_id", nullable = false)
    private Long futureContextId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GenerationStatus status;

    @Column(name = "product_name")
    private String productName;

    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Generation(Long archiveProductId, Long futureContextId) {
        this.archiveProductId = archiveProductId;
        this.futureContextId = futureContextId;
        this.status = GenerationStatus.GENERATING;
        this.createdAt = LocalDateTime.now();
    }

    public void complete(
            String productName,
            String category,
            String imageUrl,
            String description
    ) {
        this.productName = productName;
        this.category = category;
        this.imageUrl = imageUrl;
        this.description = description;
        this.status = GenerationStatus.COMPLETED;
    }

    public void fail() {
        this.status = GenerationStatus.FAILED;
    }

}
