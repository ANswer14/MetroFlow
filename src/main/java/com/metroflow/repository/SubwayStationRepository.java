package com.metroflow.repository;

import com.metroflow.model.dto.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubwayStationRepository extends JpaRepository<SubwayStation, Long> {

    @Query(value = "select station.stationName" +
            " from SubwayStation station" +
            " where station.stationLine = :line")
    List<String> findByStationName(String line);

    List<SubwayStation> findByStationNameContainingIgnoreCase(String search);

    @Query(value = "select station" +
            " from SubwayStation station" +
            " where station.stationName = :stationName")
    List<SubwayStation> findStationLineByStationName(@Param("stationName") String stationName);
}
