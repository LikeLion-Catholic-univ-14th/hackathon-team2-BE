package org.example.hackathon_team2_be.repository;

import org.example.hackathon_team2_be.domain.HeritageDna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeritageDnaRepository extends JpaRepository<HeritageDna,Long> {

    List<HeritageDna> findByArchiveProductId(Long archiveProductId);
}
