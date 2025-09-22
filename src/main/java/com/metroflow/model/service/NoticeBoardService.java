package com.metroflow.model.service;

import com.metroflow.model.dto.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeBoardService {

    private final BoardService BOARDSERVICE;

    // 긴급공지에 들어갈 게시물의 내용을 15자 이상일 시 ...으로 표시
    public String modifyBoardText(Long boardNo) {
        Board board = BOARDSERVICE.getBoardInfo(boardNo);
        String boardText = board.getBoardText();
        return boardText.length() < 15 ? boardText : boardText.substring(0, 14) + "..." ;
    }
}
