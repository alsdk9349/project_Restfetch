package com.example.backend.domain.fetch.repository;


import com.example.backend.domain.fetch.entity.Observer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObserverRepository extends JpaRepository<Observer, Long> {

    Optional<Observer> findByObserverSerialNumber (String observerSerialNumber);
}
