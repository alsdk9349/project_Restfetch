package com.example.backend.domain.fetch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "search")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id", columnDefinition = "int unsignes")
    private int id;

    @NotNull
    @Column(name = "observerSerialNumber", unique = true, length = 255)
    private String observerSerialNumber;

    @NotNull
    @Column(name = "picture", unique = true, length = 255)
    private String picture;

    @NotNull
    @Column(name = "isPick", columnDefinition = "boolean")
    private boolean isPick;

    @NotNull
    @Column(name = "createdAt", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
