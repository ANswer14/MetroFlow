package com.metroflow.repository;

import com.metroflow.model.dto.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
    @Query(value = "select count(board)" +
            " from Board board" +
            " where board.isNoticeBoard = true")
    int findCountsByIsNoticeBoard();


}
