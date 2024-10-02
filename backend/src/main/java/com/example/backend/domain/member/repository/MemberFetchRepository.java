package com.example.backend.domain.member.repository;

import com.example.backend.domain.fetch.entity.Fetch;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.entity.MemberFetch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberFetchRepository extends JpaRepository<MemberFetch, Long> {
    Optional<MemberFetch> findByFetchAndMember (Fetch fetch, Member member);
    List<MemberFetch> findByMember (Member member);
}
