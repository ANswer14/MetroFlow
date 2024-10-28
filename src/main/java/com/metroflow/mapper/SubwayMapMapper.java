package com.metroflow.mapper;

import com.metroflow.model.dto.SubwayMapInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubwayMapMapper {
    // currentColumns은 현재 시간을 time column 형식(ex h0530) 형식으로 바꿔서 가져옴
    @Select("SELECT s.station_id, s.station_name, s.station_line, ty.direction_type, ${currentColumn} AS congestion " +
            "FROM station s " +
            "JOIN type ty ON ty.station_id = s.station_id " +
            "JOIN time ti ON ti.type_id = ty.type_id " +
            "WHERE s.station_name = #{stationName} AND ty.day_type = #{dayType}")
    // 메인 화면 노선도 모달 표시 기능에 필요한 정보를 가져오는 MyBatis 매퍼
    // staion, type, time 3개의 테이블을 inner join 처리한 후 파라미터로 받아온
    // #{stationName}, #{dayType} 을 이용해 특정 역의 평일 or 휴일 데이터를 가져오는 쿼리
    // 시간 지정 불가능, LocalTime을 기반으로 현재 시간을 currentColumn으로 가져와서 String으로 변환해 사용하는 구조
    List<SubwayMapInfo> getSubwayMapInfo(@Param("stationName") String stationName,
                                     @Param("dayType") String dayType,
                                     @Param("currentColumn") String currentColumn);

}
