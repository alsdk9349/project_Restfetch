package com.example.backend.domain.member.repository;

import com.example.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByMemberId(Long memberId);
    boolean existsByMemberId(Long memberId);
}
