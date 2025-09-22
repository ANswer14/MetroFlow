package com.metroflow.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// 페이징용 보드DTO
@Getter
@Setter
public class BoardForm {
    private long boardNo;
    private User user;
    private String boardText;
    private String stationName;
    private String stationLine;
    private String title;
    private LocalDateTime createdTime;
    private Long thumbsUp;
    private Long thumbsDown;
    private Long view;
    private boolean isNoticeBoard; // 긴급 공지인지 여부

    public BoardForm(long boardNo, User user, String stationName, String stationLine, String title, LocalDateTime createdTime, Long thumbsUp, Long view, boolean isNoticeBoard) {
        this.boardNo = boardNo;
        this.user = user;
        this.stationName = stationName;
        this.stationLine = stationLine;
        this.title = title;
        this.createdTime = createdTime;
        this.thumbsUp = thumbsUp;
        this.view = view;
        this.isNoticeBoard = isNoticeBoard;
    }
}
