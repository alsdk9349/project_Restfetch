package com.example.backend.domain.fetch.entity;

import com.example.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "fetch_serial_number", length=255)
    private String fetchSerialNumber;

    @NotNull
    @Column(name = "nickname", length = 30)
    private String nickname;

    @Column(name = "battery", columnDefinition = "int")
    private int battery;

    @Builder.Default
    @OneToMany(mappedBy = "fetch")
    private List<Observer> observers = new ArrayList<>();
}
