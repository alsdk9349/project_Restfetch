package com.example.backend.domain.robot.repository;

import com.example.backend.domain.robot.entity.Fetch;
import com.example.backend.domain.robot.entity.FetchObserver;
import com.example.backend.domain.robot.entity.Observer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FetchObserverRepository extends JpaRepository<FetchObserver, Long> {

    Optional<FetchObserver> findByFetchAndObserver(Fetch fetch, Observer observer);

    List<FetchObserver> findByFetch(Fetch fetch);
}
