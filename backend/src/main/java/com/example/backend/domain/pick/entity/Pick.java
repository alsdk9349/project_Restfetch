package com.example.backend.domain.pick.entity;

import com.example.backend.domain.report.entity.Report;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pick")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Pick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_id", columnDefinition = "int unsigned")
    private int id;

    @OneToOne
    @JoinColumn(name = "report_id")
    private Report report;


}
