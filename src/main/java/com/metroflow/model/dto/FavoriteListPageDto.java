package com.metroflow.model.dto;

// SubwayTime을 통째로 쓸거라서 인터페이스로 선언하고 JPQL 선언 부분에서 맞춰줄 예정
public interface FavoriteListPageDto {

    String getUserId();
    Long getStationId();
    String getStationLine();
    String getStationName();
    String getDirectionType();
    String getDayType();

    // SubwayTime 객체 추가
    SubwayTime getSubwayTime();

}
