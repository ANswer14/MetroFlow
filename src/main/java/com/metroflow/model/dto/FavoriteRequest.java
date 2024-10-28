package com.metroflow.model.dto;

import lombok.Getter;
import lombok.Setter;

// 스크립트 측에서 보낸 즐겨찾기 등록을 위한 user_id, station_id를 받기 위한 dto
@Getter
@Setter
public class FavoriteRequest {
    private String user_id;
    private Long station_id;
}
