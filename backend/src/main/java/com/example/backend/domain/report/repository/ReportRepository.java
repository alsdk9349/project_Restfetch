package com.example.backend.domain.report.repository;

import com.example.backend.domain.robot.entity.Observer;
import com.example.backend.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByObserver(Observer observer);
}
