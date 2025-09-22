package com.metroflow.model.dao;

import com.metroflow.model.dto.Board;
import com.metroflow.model.dto.NoticeBoard;
import com.metroflow.model.service.BoardService;
import com.metroflow.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class NoticeBoardDAO {

    private final BoardService BOARDSERVICE;
    private final NoticeBoardRepository NOTICEBOARDREPOSITORY;

    // 게시물 중 긴급공지 insert
    public void insert(Long boardNo) {
        NoticeBoard notice = new NoticeBoard();
        Board board =  BOARDSERVICE.getBoardInfo(boardNo);
        notice.setBoardNo(boardNo);
        notice.setBoard(board);
        NOTICEBOARDREPOSITORY.save(notice);
    }


}
