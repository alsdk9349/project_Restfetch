package com.example.backend.domain.robot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fetch_observer")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class FetchObserver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberFetch_id", columnDefinition = "int unsigned")
    private Long fetchObserverId;  // ID 필드를 별도로 정의해야 함

    @ManyToOne
    @JoinColumn(name = "fetch_id")  // Fetch 외래 키
    private Fetch fetch;

    @ManyToOne
    @JoinColumn(name = "observer_id")  // Member 외래 키
    private Observer observer;
}
