package org.example.hackathon_team2_be.repository;

import org.example.hackathon_team2_be.domain.PresetScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PresetScenarioRepository extends JpaRepository<PresetScenario, Long> {

    Optional<PresetScenario> findByArchiveProductIdAndFutureContextId(Long archiveProductId, Long futureContextId);

    @Query("SELECT p FROM PresetScenario p WHERE " +
            "(:productName IS NOT NULL AND LOWER(:productName) LIKE CONCAT('%', LOWER(p.archiveProductName), '%')) AND " +
            "(:contextName IS NOT NULL AND LOWER(:contextName) LIKE CONCAT('%', LOWER(p.futureContextName), '%'))")
    Optional<PresetScenario> findByMatchingNames(@Param("productName") String productName, @Param("contextName") String contextName);
}