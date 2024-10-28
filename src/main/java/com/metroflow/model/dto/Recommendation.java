package com.metroflow.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId; // 기본 키 추가

    @OnDelete(action = OnDeleteAction.CASCADE) // 유저 객체 삭제 시 해당 userId와 연결되는 Recommendation 삭제
    @ManyToOne // 다대일 관계 다 : Recommendation, 일 : User
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OnDelete(action = OnDeleteAction.CASCADE) // 보드 객체 삭제 시 해당 boardNo와 연결되는 Recommendation 삭제
    @ManyToOne // 다대일 관계 다 : Recommendation, 일 : User
    @JoinColumn(name = "boardNo", nullable = false)
    private Board board;
    private boolean isThumbsUp; // 좋아요 여부
    private boolean isThumbsDown; // 싫어요 여부
}
