package com.example.backend.domain.search.repository;

import com.example.backend.domain.robot.entity.Observer;
import com.example.backend.domain.search.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByObserver(Observer observer);
}
