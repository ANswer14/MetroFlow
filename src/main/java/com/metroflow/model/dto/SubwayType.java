package com.metroflow.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "type")
public class SubwayType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long typeId;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "stationId", referencedColumnName = "stationId", nullable = false)
    private SubwayStation subwayStation;
    @Column(length = 2, nullable = false)
    private String directionType;
    @Column(length = 2, nullable = false)
    private String dayType;
}