package com.metroflow.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

// 보드 엔티티
@Entity
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardNo; // 주 식별자, sequence
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private User user; // 외래키 - userId와 매핑되어있음 userId 삭제 시 userId에 맞는 객체 삭제
    @Column(columnDefinition = "TEXT", nullable = false)
    private String boardText; // 보드 내용
    @Column(length = 15, nullable = false)
    private String stationName; // 역 이름
    @Column(length = 3, nullable = false)
    private String stationLine; // 역 호선
    @Column(length = 20, nullable = false)
    private String title; // 보드 제목
    private LocalDateTime createdTime; // 보드 게시 날짜
    @Column(columnDefinition = "bigint")
    private Long thumbsUp; // 보드 좋아요 수
    @Column(columnDefinition = "bigint")
    private Long thumbsDown; // 보드 싫어요 수
    @Column(columnDefinition = "bigint")
    private Long view; // 보드 조회수
    private boolean isNoticeBoard; // 긴급 공지인지 여부
    // 변경 사항
}