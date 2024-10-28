package com.metroflow.model.dto;

import lombok.Getter;
import lombok.Setter;
// POST 요청에 대한 응답을 반환하기 위한 DTO
// Generic 타입 선언 -> 응답으로 보낼 자료형은 해당 메소드에서 자유롭게 설정 가능
@Getter
@Setter
public class FavoriteApiResponse<T> {
    private String message;
    private T data;
    private boolean success;

    public FavoriteApiResponse(String message, T data, boolean success) {
        this.message = message;
        this.data = data;
        this.success = success;
    }
}
