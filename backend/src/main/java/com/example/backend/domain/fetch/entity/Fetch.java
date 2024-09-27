package com.example.backend.domain.fetch.entity;

import com.example.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="`fetch`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Fetch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fetch_id", columnDefinition = "int")
    private long fetchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Column(name = "fetchSerialNumber", unique = true, length=255)
    private String fetchSerialNumber;

    @NotNull
    @Column(name = "nickname", unique = true, length = 30)
    private String nickname;

    @NotNull
    @Column(name = "battery", columnDefinition = "int")
    private int battery;
}
