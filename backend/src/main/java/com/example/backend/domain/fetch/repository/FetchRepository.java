package com.example.backend.domain.fetch.repository;

import com.example.backend.domain.fetch.entity.Fetch;
import com.example.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FetchRepository extends JpaRepository<Fetch, Long> {
    Optional<Fetch> findByFetchSerialNumber (String fetchSerialNumber);

    List<Fetch> findByMember_MemberId(Long memberId);
}
