package com.example.backend.domain.pick.repository;

import com.example.backend.domain.pick.entity.Pick;
import com.example.backend.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PickRepository extends JpaRepository<Pick, Long> {
    Optional<Pick> findByReport(Report report);

}
