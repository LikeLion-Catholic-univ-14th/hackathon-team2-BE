package org.example.hackathon_team2_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class HeritageDna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_product_id", nullable = false)
    private ArchiveProduct archiveProduct;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private int ratio;

    public HeritageDna(
            ArchiveProduct archiveProduct,
            String name,
            String description,
            int ratio
    ) {
        this.archiveProduct = archiveProduct;
        this.name = name;
        this.description = description;
        this.ratio = ratio;
    }
}
