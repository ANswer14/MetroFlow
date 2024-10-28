package com.metroflow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {

    private String StationName;
    private String stationLine;
    private String ampm;
    private String hour;
    private String minute;
}
