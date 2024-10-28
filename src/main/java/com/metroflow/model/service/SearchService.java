package com.metroflow.model.service;

import com.metroflow.mapper.SearchMapper;
import com.metroflow.model.dto.SearchResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SearchService {

    private final SearchMapper searchMapper;

    @Autowired
    private final IsHolidaysService isHolidaysService;


    // SearchMapper 주입 (생성자 주입)
    public SearchService(SearchMapper searchMapper, IsHolidaysService isHolidaysService) {
        this.searchMapper = searchMapper;
        this.isHolidaysService = isHolidaysService;
    }


    public List<SearchResultInfo> getSearchResults(String stationName, String stationLine, String ampm, String hour, String minute){
        String inputColumn = getInputColumn(ampm, hour, minute);
        String dayType = getCurrentDayType();
        return searchMapper.getSearchResultInfo(stationName, dayType, inputColumn, stationLine);
    }

    // 데이터를 가져오는 Service 메서드
    public List<SearchResultInfo> getSearchResultInfo(String stationName, String stationLine, String dayType, String inputColumn) {
        return searchMapper.getSearchResultInfo(stationName, dayType, inputColumn, stationLine);
    }

    private String getCurrentDayType(){
        LocalDate now = LocalDate.now();
        return isHolidaysService.classifyDate(now);
    }

    public String getInputColumn(String ampm, String hour, String minute) {

            int hourInt = Integer.parseInt(hour);
            int minuteInt = Integer.parseInt(minute);

            if ("PM".equalsIgnoreCase(ampm) && hourInt < 12) {
                hourInt += 12;
            } else if ("AM".equalsIgnoreCase(ampm) && hourInt == 12) {
                hourInt = 0;
            }

            if (minuteInt >= 30) {
                minuteInt = 30;
            } else {
                minuteInt = 0;
            }

            return String.format("h%02d%02d", hourInt, minuteInt);
        }
    }

