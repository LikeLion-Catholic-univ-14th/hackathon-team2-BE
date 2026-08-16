package org.example.hackathon_team2_be.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "generation_locked_dna")
@Getter
@NoArgsConstructor
public class GenerationLockedDna {

    @EmbeddedId
    private GenerationLockedDnaId id;

    public GenerationLockedDna(Long generationId, Long heritageDnaId) {
        this.id = new GenerationLockedDnaId(generationId, heritageDnaId);
    }
}
