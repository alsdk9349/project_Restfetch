package com.example.backend.domain.robot.repository;

import com.example.backend.domain.robot.entity.Fetch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FetchRepository extends JpaRepository<Fetch, Long> {
    Optional<Fetch> findByFetchSerialNumber (String fetchSerialNumber);

}
