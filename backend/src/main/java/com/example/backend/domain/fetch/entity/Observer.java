package com.example.backend.domain.fetch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Set;

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
    @Column(name = "observer_serial_number", length = 255)
    private String observerSerialNumber;

    @NotNull
    @Column(name = "location", columnDefinition = "point")
    private Point2D.Double location;

    @ManyToOne
    @JoinColumn(name="fetch_id", columnDefinition = "int unsigned")
    private Fetch fetch;
}
