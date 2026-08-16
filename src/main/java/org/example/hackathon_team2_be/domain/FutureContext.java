package org.example.hackathon_team2_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "future_contexts")
public class FutureContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    public FutureContext(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
