package com.metroflow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubwayMapInfo {
    // 홈 노선도 화면에 사용하는 DTO
    // Entity 선언 필요하지 않음
    private Long station_id;
    private String station_name;
    private String station_line;
    private String direction_type;
    private String congestion; // 동적으로 가져오는 시간 정보
    // Getters and Setters
}
