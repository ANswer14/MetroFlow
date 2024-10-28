package com.metroflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TimeChecker {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class CanlendarDTO{
        String year;
        String month;
        String day;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ClockDTO {
        String ampm;
        String hour;
        String minute;
    }
}
