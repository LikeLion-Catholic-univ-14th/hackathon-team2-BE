package org.example.hackathon_team2_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "archive_product_tags")
public class ArchiveProductTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_product_id", nullable = false)
    private ArchiveProduct archiveProduct;

    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;
}
