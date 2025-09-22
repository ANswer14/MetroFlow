package com.metroflow.controller;

import com.metroflow.model.dao.BoardDAO;
import com.metroflow.model.dto.Board;
import com.metroflow.model.dto.BoardForm;
import com.metroflow.model.dto.RecommendationRequestForm;
import com.metroflow.model.service.BoardService;
import com.metroflow.model.service.UserService;
import com.metroflow.repository.BoardRepository;
import com.metroflow.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService BOARDSERVICE;
    private final UserService USERSERVICE;
    private final BoardDAO BOARDDAO;
    private final BoardRepository BOARDREPOSITORY;
    private final NoticeBoardRepository NOTICEBOARDREPOSITORY;
    // /board?page=1
    // 보드 페이징 처리 컨트롤러
    @GetMapping("/board")
    public String goBoard(@PageableDefault(page = 1) Pageable pageable, Model model, @RequestParam(value = "selectedOption", required = false) String option, @RequestParam(value = "boardOption", required = false) String boardOption) {
        Page<BoardForm> boardList; // 보여줄 리스트
        Page<BoardForm> allBoards = BOARDSERVICE.allBoardsPaging(pageable); // 모든 게시판을 담은 리스트
        Page<BoardForm> myBoards = BOARDSERVICE.myBoardsPaging(pageable); // 세션 유저가 쓴 게시판을 담은 리스트
        // boardOption : 게시판 화면에서 게시판 보기 옵션 선택값 처음 보드 들어올 땐 null값
        // selectedOption : HTML 내에서 select 옵션이 바뀔 시 그 값을 전달받음(처음 들어올 땐 null값)
        // 둘 중 하나는 무조건 null
        if (!Objects.isNull(option)) {
            if (option.equals("myBoards")) {
                boardList = myBoards;
                option = "myBoards";
            } else {
                boardList = allBoards;
                option = "allBoards";
            }
        } else if(!Objects.isNull(boardOption)) {
            if (boardOption.equals("myBoards")) {
                boardList = myBoards;
                option = "myBoards";
            } else {
                boardList = allBoards;
                option = "allBoards";
            }
        } else {
            boardList = allBoards;
            option = "allBoards";
        }

        int blockLimit = 5; // 한번에 보일 페이지 갯수 제한
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = Math.min((startPage + blockLimit - 1), boardList.getTotalPages());
        int noticeCount =  NOTICEBOARDREPOSITORY.findCountsByIsNoticeBoard(); // 공지 수 가져오기

        model.addAttribute("selectedOption", option); // 선택된 옵션
        model.addAttribute("noticeCount", noticeCount); // 공지 수
        model.addAttribute("boardList", boardList); // 보드 리스트
        model.addAttribute("startPage", startPage); // 시작 페이지
        model.addAttribute("endPage", endPage); // 끝 페이지
        model.addAttribute("sessionUser", USERSERVICE.getUserObject()); // 세션 유저 객체
        return "board/board"; // 보드
    }

    // 보드 작성 공간으로 이동해주는 컨트롤러
    @GetMapping("/board/writeBoard")
    public String goWrite(Model model, Board board) {
        Map<String, List<String>> stationMap =  BOARDSERVICE.getStationNames();
        board.setUser(USERSERVICE.getUserObject());
        model.addAttribute("station", stationMap); // 작성란 위의 옵션들에 값을 채워넣기 위함(호선 당 역)
        model.addAttribute("board", board);
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        return "board/boardToWrite"; // 보드 작성 공간
    }

    // 보드 작성란에서 작성 후 글쓰기 누를 시 아래 컨트롤러 작동
    // 글쓰기 컨트롤러
    @PostMapping("/board/write")
    public String write(@ModelAttribute Board board) {
        BOARDDAO.writeBoard(board); // insert해주는 DAO
        return "redirect:/board"; // "/board"를 GET방식으로 받는 컨트롤러(goBoard)로 감
    }

    // 보드 제목 클릭 시 해당 보드의 내용으로 가게 해주는 컨트롤러
    @GetMapping("/board/content")
    public String viewContent(@RequestParam("boardNo") Long no, Model model) {
        BOARDREPOSITORY.plusView(no); // 조회수 추가 메소드
        BOARDDAO.insertRecommendation(no); // 추천 테이블속 유저에게 해당하는 테이블 없을 시 생성, 있다면 void 반환
        model.addAttribute("board", BOARDSERVICE.getBoardInfo(no));
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        model.addAttribute("userRec", BOARDSERVICE.getMyRecommendation(no));
        return "board/boardContent"; // 보드 내용
    }

    // 자신의 글을 수정할 때 글 수정버튼 누르면 아래 컨트롤러 작동
    // 해당 글의 boardNo를 파라미터로 받아와 수정준비를 하는 컨트롤러
    @GetMapping("/board/updateBoard")
    public String goUpdate(@RequestParam("no") Long no, Model model) {
        Map<String, List<String>> stationMap =  BOARDSERVICE.getStationNames();
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        model.addAttribute("board", BOARDSERVICE.getBoardInfo(no));
        model.addAttribute("station", stationMap);
        return "board/boardToWrite"; // 작성 화면
    }

    // 글 수정버튼 누를 때 아래 컨트롤러 작동
    // 글 수정 컨트롤러
    @PostMapping("/board/update")
    public String update(@ModelAttribute("board") Board board, Model model) {
        BOARDREPOSITORY.updateBoard(board.getBoardText(), board.getStationLine(), board.getStationName(), board.getTitle(), board.getBoardNo());
        model.addAttribute("board", BOARDSERVICE.getBoardInfo(board.getBoardNo()));
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        model.addAttribute("userRec", BOARDSERVICE.getMyRecommendation(board.getBoardNo()));
        return "board/boardContent"; // 보드 수정이 완료된 내용 화면
    }

    // 글 삭제버튼 누르면 아래 컨트롤러 작동
    // 글 삭제 컨트롤러
    @GetMapping("/board/delete")
    public String delete(@RequestParam("no") Long no) {
        BOARDREPOSITORY.deleteById(no); // 보드 번호에 맞는 해당 글 삭제
        return "redirect:/board"; // 보드 화면으로 redirect
    }

    // 좋아요 수 관련 로직 컨트롤러
    @PostMapping("/board/recommendation")
    public ResponseEntity<String> recommendation(@RequestBody RecommendationRequestForm rec) {
        Long boardNo = rec.getBoardNo(); // 해당 보드의 숫자
        BOARDDAO.updateRecommendation(boardNo, rec.isThumbsUp(), rec.isThumbsDown(), rec.isPriorThumbsUp(), rec.isPriorThumbsDown()); // 좋아요나 싫어요를 누른 결과에 따라 DB에 좋아요, 싫어요 수 반영
        return ResponseEntity.ok("success");
    }
}
