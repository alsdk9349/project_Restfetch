package com.example.backend.domain.fetch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "observer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Observer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "observer_id", columnDefinition = "int unsigned")
    private long observer_id;

    @NotNull
    @Column(name = "observerSerialNumber", unique = true, length = 255)
    private String observerSerialNumber;
}
