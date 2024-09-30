package com.example.backend.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", columnDefinition = "int unsigned")
    private long memberId;

    @NotNull
    @Column(name = "nickname", unique = true, length = 20)
    private String nickname;

    @NotNull
    @Column(name = "email", unique = true, length = 100)
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "token", length = 500)
    private String token;

    @OneToMany(mappedBy = "member")
    private List<MemberFetch> fetches;

}
