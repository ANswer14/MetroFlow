package com.metroflow.controller;

import com.metroflow.model.dto.UserForm;
import com.metroflow.model.service.UserService;
import com.metroflow.model.dao.NoticeBoardDAO;
import com.metroflow.repository.BoardRepository;
import com.metroflow.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService USERSERVICE;
    private final BoardRepository BOARDREPOSITORY;
    private final NoticeBoardDAO NOTICEBOARDDAO;
    private final NoticeBoardRepository NOTICEBOARDREPOSITORY;

    // 관리자 계정으로 게시물 삭제(삭제 컨트롤러로 보내주는 메소드)
    @GetMapping("/admin/board/delete")
    public ResponseEntity<String> boardDelete(@RequestParam("boardNo") Long boardNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/board/delete?no=" + boardNo));
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // redirection을 하겠다는 의미, 이것만 있으면 작동 X, js에서 다뤄줘야 함(response.redirected)
    }

    // 관리자 계정으로 게시물 긴급공지로 insert
    @GetMapping("/admin/notice/insert")
    public ResponseEntity<String> noticeInsert(@RequestParam("boardNo") Long boardNo) {
        NOTICEBOARDDAO.insert(boardNo);
        BOARDREPOSITORY.updateBoardByBoardNo(true, boardNo);
        return ResponseEntity.ok().body("게시글 인서트 성공!");
    }

    // 관리자 계정으로 게시물 긴급공지 삭제
    @GetMapping("/admin/notice/delete")
    public ResponseEntity<String> noticeDelete(@RequestParam("boardNo") Long boardNo) {
        NOTICEBOARDREPOSITORY.deleteById(boardNo);
        BOARDREPOSITORY.updateBoardByBoardNo(false, boardNo);
        return ResponseEntity.ok().body("공지 삭제 성공!");
    }

    // 관리자 계정으로 유저 리스트 보기(페이징 처리 O)
    @GetMapping("/admin/userList")
    public String goUserList(Model model, @PageableDefault(page = 1) Pageable pageable) {
        Page<UserForm> userList = USERSERVICE.paging(pageable); // 페이징 처리된 보드들 리스트

        int blockLimit = 5; // 한번에 보일 페이지 갯수 제한
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = Math.min((startPage + blockLimit - 1), userList.getTotalPages());

        model.addAttribute("userList", userList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        return "admin/userList";
    }
}
