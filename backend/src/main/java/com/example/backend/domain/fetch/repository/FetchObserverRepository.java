package com.example.backend.domain.fetch.repository;

import com.example.backend.domain.fetch.entity.Fetch;
import com.example.backend.domain.fetch.entity.FetchObserver;
import com.example.backend.domain.fetch.entity.Observer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FetchObserverRepository extends JpaRepository<FetchObserver, Long> {

    Optional<FetchObserver> findByFetchAndObserver(Fetch fetch, Observer observer);
}
