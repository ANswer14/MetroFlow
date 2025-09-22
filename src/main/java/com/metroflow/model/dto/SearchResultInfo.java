package com.metroflow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResultInfo {

    private Long stationId;
    private String stationName;
    private String stationLine;
    private String directionType;
    private String congestion;
}
