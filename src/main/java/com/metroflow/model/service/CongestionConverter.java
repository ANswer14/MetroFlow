package com.metroflow.model.service;

import org.springframework.stereotype.Component;

@Component
public class CongestionConverter {

    public static Integer convertToInt(String congestion) {
        try {
            // 입력값이 null이거나 빈 문자열일 경우
            if (congestion == null || congestion.isEmpty()) {
                return 0; // 또는 적절한 기본값을 반환
            }
            return Integer.parseInt(congestion);
        } catch (NumberFormatException e) {
            // 예외 발생 시 처리 (로깅 등)
            System.err.println("At Congestion Converter : Invalid congestion value: " + congestion);
            return 0; // 또는 적절한 기본값을 반환
        }
    }
}

