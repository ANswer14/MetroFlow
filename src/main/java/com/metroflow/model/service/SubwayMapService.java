package com.metroflow.model.service;

import com.metroflow.mapper.SubwayMapMapper;
import com.metroflow.model.dto.SubwayMapInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SubwayMapService {

    private final SubwayMapMapper subwayMapMapper;

    private final IsHolidaysService isHolidaysService;

    @Autowired
    private SubwayMapService(SubwayMapMapper subwayMapMapper, IsHolidaysService isHolidaysService) {
        this.subwayMapMapper = subwayMapMapper;
        this.isHolidaysService = isHolidaysService;

        validSet.add("h0530");
        validSet.add("h0600");
        validSet.add("h0630");
        validSet.add("h0700");
        validSet.add("h0730");
        validSet.add("h0800");
        validSet.add("h0830");
        validSet.add("h0900");
        validSet.add("h0930");
        validSet.add("h1000");
        validSet.add("h1030");
        validSet.add("h1100");
        validSet.add("h1130");
        validSet.add("h1200");
        validSet.add("h1230");
        validSet.add("h1300");
        validSet.add("h1330");
        validSet.add("h1400");
        validSet.add("h1430");
        validSet.add("h1500");
        validSet.add("h1530");
        validSet.add("h1600");
        validSet.add("h1630");
        validSet.add("h1700");
        validSet.add("h1730");
        validSet.add("h1800");
        validSet.add("h1830");
        validSet.add("h1900");
        validSet.add("h1930");
        validSet.add("h2000");
        validSet.add("h2030");
        validSet.add("h2100");
        validSet.add("h2130");
        validSet.add("h2200");
        validSet.add("h2230");
        validSet.add("h2300");
        validSet.add("h2330");
        validSet.add("h0000");
        validSet.add("h0030");
    }

    public List<SubwayMapInfo> getSubwayMapInfo(String stationName){
        String currentColumn = getCurrentTimeColumn();
        String dayType = getCurrentDayType();
        return subwayMapMapper.getSubwayMapInfo(stationName, dayType, currentColumn);
    }

    private final Set<String> validSet = new HashSet<>();


    //현재 시간을 LocalTime으로 받아와서 h0000 형식으로 반환하는 메소드
    public String getCurrentTimeColumn(){
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();

        //30분 단위로 자르기
        if(minute>=30){
            minute=30;
        }
        else{
            minute=0;
        }

        // 유효한 시간 범위 체크
        if (hour >= 1 && hour <= 5) {
            System.err.println("SubwayMapService : 유효하지 않은 시간대입니다.");
            return "h0030"; // h0100 ~ h0500은 리턴하지 않음
        }

        // h0000 ~ h2330의 유효한 시간 반환
        if (hour >= 0 && hour <= 23) {
            return String.format("h%02d%02d", hour, minute);
        }
        System.err.println("SubwayMapService : 유효하지 않은 시간대입니다.");
        return "h0030";
    }
    // 평일, 휴일인지 분류해서 String으로 리턴하는 메소드
    public String getCurrentDayType(){
        LocalDate now = LocalDate.now();
        String result = isHolidaysService.classifyDate(now);
//        System.out.println("subwayMapService getCurrentDayType : " + result);
        return result;
    }

    // h0000 꼴의 입력이 올바른 입력인지 체크해주는 함수
    public String isValidTimeFormat(String input){
        if(validSet.contains(input)){
            return input;
        }
        else {
            System.err.println("at SubWayMap Service, isValidTimeFormate : Wrong input");
            return "h0030";
        }
    }

}
