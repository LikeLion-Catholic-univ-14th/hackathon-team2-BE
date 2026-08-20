package org.example.hackathon_team2_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "preset_scenarios")
public class PresetScenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "archive_product_id")
    private Long archiveProductId;

    @Column(name = "future_context_id")
    private Long futureContextId;

    @Column(name = "archive_product_name")
    private String archiveProductName;

    @Column(name = "future_context_name")
    private String futureContextName;

    @Column(name = "product_name")
    private String productName;

    private String category;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;
}