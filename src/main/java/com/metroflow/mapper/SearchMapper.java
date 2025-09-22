package com.metroflow.mapper;

import com.metroflow.model.dto.SearchResultInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchMapper {


    @Select("SELECT s.station_name AS stationName, s.station_line AS stationLine, ty.direction_type AS directionType, ${inputColumn} AS congestion " +
            "FROM station s " +
            "JOIN type ty ON ty.station_id = s.station_id " +
            "JOIN time ti ON ti.type_id = ty.type_id " +
            "WHERE s.station_name = #{stationName} AND ty.day_type = #{dayType} AND s.station_line = #{stationLine}")
    List<SearchResultInfo> getSearchResultInfo(@Param("stationName") String stationName,
                                               @Param("dayType") String dayType,
                                               @Param("inputColumn") String currentColumn,
                                               @Param("stationLine") String stationLine);
}


