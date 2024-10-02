package com.example.backend.domain.search.entity;

import com.example.backend.domain.robot.entity.Observer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", columnDefinition = "int unsigned")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "observer_id")
    private Observer observer;

    @NotNull
    @Column(name = "picture", length = 255)
    private String picture;

    @NotNull
    @Column(name = "isPicked", columnDefinition = "boolean")
    private boolean isPicked;

    @NotNull
    @Column(name = "createdAt", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
