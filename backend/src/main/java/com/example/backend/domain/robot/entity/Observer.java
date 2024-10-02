package com.example.backend.domain.robot.entity;

import com.example.backend.domain.search.entity.Report;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.awt.*;
import java.util.List;

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
    @Column(name = "latitude", columnDefinition = "double")
    private double latitude;

    @NotNull
    @Column(name = "longitude", columnDefinition = "double")
    private double longitude;

    @OneToMany(mappedBy = "observer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FetchObserver> observers;
    
    @OneToMany(mappedBy = "observer")
    private List<Report> reports;
}
