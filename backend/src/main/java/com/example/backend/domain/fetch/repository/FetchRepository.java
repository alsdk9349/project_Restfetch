package com.example.backend.domain.fetch.repository;

import com.example.backend.domain.fetch.entity.Fetch;
import com.example.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FetchRepository extends JpaRepository<Fetch, Long> {
    Optional<Fetch> findByFetchSerialNumberAndMember_MemberId (String fetchSerialNumber, Long memberId);

    List<Fetch> findByMember_MemberId(Long memberId);
}
