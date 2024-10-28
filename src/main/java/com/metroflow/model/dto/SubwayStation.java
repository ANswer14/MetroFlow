package com.metroflow.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Station")
public class SubwayStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stationId;
    @Column(length = 15, nullable = false)
    private String stationName;
    @Column(length = 1, nullable = false)
    private String stationLine;
}