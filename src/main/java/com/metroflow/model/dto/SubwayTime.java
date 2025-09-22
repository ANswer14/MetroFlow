package com.metroflow.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "Time")
public class SubwayTime {
    @Id
    @Column(name = "typeId")
    private long typeId;

    @MapsId
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "typeId", referencedColumnName = "typeId")
    private SubwayType subwayType;

    @Column(length = 3)
    private String h0530;
    @Column(length = 3)
    private String h0600;
    @Column(length = 3)
    private String h0630;
    @Column(length = 3)
    private String h0700;
    @Column(length = 3)
    private String h0730;
    @Column(length = 3)
    private String h0800;
    @Column(length = 3)
    private String h0830;
    @Column(length = 3)
    private String h0900;
    @Column(length = 3)
    private String h0930;
    @Column(length = 3)
    private String h1000;
    @Column(length = 3)
    private String h1030;
    @Column(length = 3)
    private String h1100;
    @Column(length = 3)
    private String h1130;
    @Column(length = 3)
    private String h1200;
    @Column(length = 3)
    private String h1230;
    @Column(length = 3)
    private String h1300;
    @Column(length = 3)
    private String h1330;
    @Column(length = 3)
    private String h1400;
    @Column(length = 3)
    private String h1430;
    @Column(length = 3)
    private String h1500;
    @Column(length = 3)
    private String h1530;
    @Column(length = 3)
    private String h1600;
    @Column(length = 3)
    private String h1630;
    @Column(length = 3)
    private String h1700;
    @Column(length = 3)
    private String h1730;
    @Column(length = 3)
    private String h1800;
    @Column(length = 3)
    private String h1830;
    @Column(length = 3)
    private String h1900;
    @Column(length = 3)
    private String h1930;
    @Column(length = 3)
    private String h2000;
    @Column(length = 3)
    private String h2030;
    @Column(length = 3)
    private String h2100;
    @Column(length = 3)
    private String h2130;
    @Column(length = 3)
    private String h2200;
    @Column(length = 3)
    private String h2230;
    @Column(length = 3)
    private String h2300;
    @Column(length = 3)
    private String h2330;
    @Column(length = 3, nullable = true)
    private String h0000;
    @Column(length = 3, nullable = true)
    private String h0030;
}