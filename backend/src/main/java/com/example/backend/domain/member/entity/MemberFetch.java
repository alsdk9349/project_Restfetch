package com.example.backend.domain.member.entity;

import com.example.backend.domain.fetch.entity.Fetch;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "member_fetch")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberFetch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberFetch_id", columnDefinition = "int unsigned")
    private Long memberFetchId;  // ID 필드를 별도로 정의해야 함

    @ManyToOne
    @JoinColumn(name = "member_id")  // Member 외래 키
    private Member member;

    @ManyToOne
    @JoinColumn(name = "fetch_id")  // Fetch 외래 키
    private Fetch fetch;

}