package com.metroflow.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class StationInfoResponse {

    private List<SubwayMapInfo> stationInfoList;

    // lombok으로 사용할 때 boolean 타입 변수에 is prefix가 들어가면
    // getter, setter 생성할 때 getIsFavorite 이 아닌 isFavorite으로 생성함 : JavaBeans 규약을 따르는 형태
    // 이 상태로 그냥 json 파싱을 위해 jackson을 사용하면 boolean 필드의 getter 메소드를 확인하고
    // prefix로 인식된 요소들을 날려버림(is, get 등등)
    // 그거 방지하기 위해 JsonPropery 어노테이션 사용
    @JsonProperty("isFavorite")
    private boolean isFavorite;

    private List<Long> favoriteStationIdList;

    // 생성자
    public StationInfoResponse(List<SubwayMapInfo> stationInfoList, boolean isFavorite, List<Long> favoriteStationIdList) {
        this.stationInfoList = stationInfoList;
        this.isFavorite = isFavorite;
        this.favoriteStationIdList = favoriteStationIdList;
    }

}
