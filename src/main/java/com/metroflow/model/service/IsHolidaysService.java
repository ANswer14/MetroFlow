package com.metroflow.model.service;

import com.metroflow.model.dto.TimeChecker;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
// 주말인지 판단하는 서비스
@Service
public class IsHolidaysService {

    private final Set<LocalDate> holidays;

    public IsHolidaysService() {
        holidays = new HashSet<>();

        holidays.add(LocalDate.of(2022, 1, 1));
        holidays.add(LocalDate.of(2022, 1, 31));
        holidays.add(LocalDate.of(2022, 2, 1));
        holidays.add(LocalDate.of(2022, 2, 2));
        holidays.add(LocalDate.of(2022, 3, 1));
        holidays.add(LocalDate.of(2022, 5, 5));
        holidays.add(LocalDate.of(2022, 5, 8));
        holidays.add(LocalDate.of(2022, 6, 6));
        holidays.add(LocalDate.of(2022, 8, 15));
        holidays.add(LocalDate.of(2022, 9, 9));
        holidays.add(LocalDate.of(2022, 9, 10));
        holidays.add(LocalDate.of(2022, 9, 11));
        holidays.add(LocalDate.of(2022, 10, 3));
        holidays.add(LocalDate.of(2022, 12, 25));

        holidays.add(LocalDate.of(2023, 1, 1));
        holidays.add(LocalDate.of(2023, 1, 21));
        holidays.add(LocalDate.of(2023, 1, 22));
        holidays.add(LocalDate.of(2023, 1, 23));
        holidays.add(LocalDate.of(2023, 3, 1));
        holidays.add(LocalDate.of(2023, 5, 5));
        holidays.add(LocalDate.of(2023, 5, 27));
        holidays.add(LocalDate.of(2023, 6, 6));
        holidays.add(LocalDate.of(2023, 8, 15));
        holidays.add(LocalDate.of(2023, 9, 28));
        holidays.add(LocalDate.of(2023, 9, 29));
        holidays.add(LocalDate.of(2023, 9, 30));
        holidays.add(LocalDate.of(2023, 10, 3));
        holidays.add(LocalDate.of(2023, 12, 25));

        holidays.add(LocalDate.of(2024, 1, 1));  // 새해
        holidays.add(LocalDate.of(2024, 2, 9));  // 설날
        holidays.add(LocalDate.of(2024, 2, 10)); // 설날
        holidays.add(LocalDate.of(2024, 2, 11)); // 설날
        holidays.add(LocalDate.of(2024, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2024, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2024, 5, 15)); // 스승의 날
        holidays.add(LocalDate.of(2024, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2024, 8, 15));  // 광복절
        holidays.add(LocalDate.of(2024, 9, 16)); // 추석
        holidays.add(LocalDate.of(2024, 9, 17)); // 추석
        holidays.add(LocalDate.of(2024, 9, 18)); // 추석
        holidays.add(LocalDate.of(2024, 10, 3));  // 개천절
        holidays.add(LocalDate.of(2024, 10, 9));  // 한글날
        holidays.add(LocalDate.of(2024, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2025, 1, 1));  // 새해
        holidays.add(LocalDate.of(2025, 1, 28)); // 설날
        holidays.add(LocalDate.of(2025, 1, 29)); // 설날
        holidays.add(LocalDate.of(2025, 1, 30)); // 설날
        holidays.add(LocalDate.of(2025, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2025, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2025, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2025, 8, 15));  // 광복절
        holidays.add(LocalDate.of(2025, 10, 3));  // 개천절
        holidays.add(LocalDate.of(2025, 10, 5));  // 추석
        holidays.add(LocalDate.of(2025, 10, 6));  // 추석
        holidays.add(LocalDate.of(2025, 10, 7));  // 추석
        holidays.add(LocalDate.of(2025, 10, 9));  // 한글날
        holidays.add(LocalDate.of(2025, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2026, 1, 1));  // 새해
        holidays.add(LocalDate.of(2026, 2, 16)); // 설날
        holidays.add(LocalDate.of(2026, 2, 17)); // 설날
        holidays.add(LocalDate.of(2026, 2, 18)); // 설날
        holidays.add(LocalDate.of(2026, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2026, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2026, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2026, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2026, 9, 24)); // 추석
        holidays.add(LocalDate.of(2026, 9, 25)); // 추석
        holidays.add(LocalDate.of(2026, 9, 26)); // 추석
        holidays.add(LocalDate.of(2026, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2026, 10, 9));  // 한글날
        holidays.add(LocalDate.of(2026, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2027, 1, 1));  // 새해
        holidays.add(LocalDate.of(2027, 2, 6));  // 설날
        holidays.add(LocalDate.of(2027, 2, 7));  // 설날
        holidays.add(LocalDate.of(2027, 2, 8));  // 설날
        holidays.add(LocalDate.of(2027, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2027, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2027, 5, 13)); // 부처님오신날
        holidays.add(LocalDate.of(2027, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2027, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2027, 9, 14)); // 추석
        holidays.add(LocalDate.of(2027, 9, 15)); // 추석
        holidays.add(LocalDate.of(2027, 9, 16)); // 추석
        holidays.add(LocalDate.of(2027, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2027, 10, 9));  // 한글날
        holidays.add(LocalDate.of(2027, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2028, 1, 1));  // 새해
        holidays.add(LocalDate.of(2028, 1, 26)); // 설날
        holidays.add(LocalDate.of(2028, 1, 27)); // 설날
        holidays.add(LocalDate.of(2028, 1, 28)); // 설날
        holidays.add(LocalDate.of(2028, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2028, 5, 2));  // 석가탄신일
        holidays.add(LocalDate.of(2028, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2028, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2028, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2028, 10, 2)); // 추석
        holidays.add(LocalDate.of(2028, 10, 3)); // 추석 개천절
        holidays.add(LocalDate.of(2028, 10, 4)); // 추석
        holidays.add(LocalDate.of(2028, 10, 9));  // 한글날
        holidays.add(LocalDate.of(2028, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2029, 1, 1));  // 새해
        holidays.add(LocalDate.of(2029, 2, 12)); // 설날
        holidays.add(LocalDate.of(2029, 2, 13)); // 설날
        holidays.add(LocalDate.of(2029, 2, 14)); // 설날
        holidays.add(LocalDate.of(2029, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2029, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2029, 5, 20)); // 석가탄신일
        holidays.add(LocalDate.of(2029, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2029, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2029, 9, 21)); // 추석
        holidays.add(LocalDate.of(2029, 9, 22)); // 추석
        holidays.add(LocalDate.of(2029, 9, 23)); // 추석
        holidays.add(LocalDate.of(2029, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2029, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2029, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2030, 1, 1));  // 새해
        holidays.add(LocalDate.of(2030, 2, 2));  // 설날
        holidays.add(LocalDate.of(2030, 2, 3));  // 설날
        holidays.add(LocalDate.of(2030, 2, 4));  // 설날
        holidays.add(LocalDate.of(2030, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2030, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2030, 5, 9));  // 석가탄실일
        holidays.add(LocalDate.of(2030, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2030, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2030, 9, 11)); // 추석
        holidays.add(LocalDate.of(2030, 9, 12)); // 추석
        holidays.add(LocalDate.of(2030, 9, 13)); // 추석
        holidays.add(LocalDate.of(2030, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2030, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2030, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2031, 1, 1));  // 새해
        holidays.add(LocalDate.of(2031, 1, 22)); // 설날
        holidays.add(LocalDate.of(2031, 1, 23)); // 설날
        holidays.add(LocalDate.of(2031, 1, 24)); // 설날
        holidays.add(LocalDate.of(2031, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2031, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2031, 5, 28)); // 석가탄신일
        holidays.add(LocalDate.of(2031, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2031, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2031, 9, 30)); // 추석
        holidays.add(LocalDate.of(2031, 10, 1)); // 추석
        holidays.add(LocalDate.of(2031, 10, 2)); // 추석
        holidays.add(LocalDate.of(2031, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2031, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2031, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2032, 1, 1));  // 새해
        holidays.add(LocalDate.of(2032, 2, 10)); // 설날
        holidays.add(LocalDate.of(2032, 2, 11)); // 설날
        holidays.add(LocalDate.of(2032, 2, 12)); // 설날
        holidays.add(LocalDate.of(2032, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2032, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2032, 5, 16)); // 석가탄신일
        holidays.add(LocalDate.of(2032, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2032, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2032, 9, 18)); // 추석
        holidays.add(LocalDate.of(2032, 9, 19)); // 추석
        holidays.add(LocalDate.of(2032, 9, 20)); // 추석
        holidays.add(LocalDate.of(2032, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2032, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2032, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2033, 1, 1));  // 새해
        holidays.add(LocalDate.of(2033, 1, 30)); // 설날
        holidays.add(LocalDate.of(2033, 1, 31)); // 설날
        holidays.add(LocalDate.of(2033, 2, 1));  // 설날
        holidays.add(LocalDate.of(2033, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2033, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2033, 5, 6));  // 석가탄신일
        holidays.add(LocalDate.of(2033, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2033, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2033, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2033, 10, 6)); // 추석
        holidays.add(LocalDate.of(2033, 10, 7)); // 추석
        holidays.add(LocalDate.of(2033, 10, 8)); // 추석
        holidays.add(LocalDate.of(2033, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2033, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2034, 1, 1));  // 새해
        holidays.add(LocalDate.of(2034, 2, 18)); // 설날
        holidays.add(LocalDate.of(2034, 2, 19)); // 설날
        holidays.add(LocalDate.of(2034, 2, 20)); // 설날
        holidays.add(LocalDate.of(2034, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2034, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2034, 5, 25)); // 석가탄신일
        holidays.add(LocalDate.of(2034, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2034, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2034, 9, 26)); // 추석
        holidays.add(LocalDate.of(2034, 9, 27)); // 추석
        holidays.add(LocalDate.of(2034, 9, 28)); // 추석
        holidays.add(LocalDate.of(2034, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2034, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2034, 12, 25)); // 크리스마스
    }

    public boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public boolean isHolidays(LocalDate date) {
        return holidays.contains(date);
    }

    public String classifyDate(LocalDate date) {
        if (isWeekend(date) || isHolidays(date)) {
            return "휴일";
        } else {
            return "평일";
        }
    }

    public TimeChecker.ClockDTO getCurrentTimeAttributes() {
        LocalDateTime now = LocalDateTime.now();

        String amPm = now.format(DateTimeFormatter.ofPattern("a", Locale.ENGLISH));
        String hour = now.format(DateTimeFormatter.ofPattern("hh"));
        String minute = now.format(DateTimeFormatter.ofPattern("mm"));

        TimeChecker timeChecker = new TimeChecker();

        return timeChecker.new ClockDTO(amPm, hour, minute);
    }

}
