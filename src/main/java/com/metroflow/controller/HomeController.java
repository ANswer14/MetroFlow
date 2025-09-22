package com.metroflow.controller;

import com.metroflow.model.dto.NoticeBoard;
import com.metroflow.model.service.NoticeBoardService;
import com.metroflow.model.service.UserService;
import com.metroflow.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService USERSERVICE;
    private final NoticeBoardRepository NOTICEBOARDREPOSITORY;
    private final NoticeBoardService NOTICEBOARDSERVICE;

    // 홈으로 가는 메소드(긴급공지 관련 로직O)
    @GetMapping("/")
    public String goHome(Model model/*, HttpSession session*/) {
//        session.invalidate();
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        List<NoticeBoard> notices = NOTICEBOARDREPOSITORY.findAll(Sort.by(Sort.Direction.DESC, "board.view"));
        for (NoticeBoard notice : notices) {
            String contents = NOTICEBOARDSERVICE.modifyBoardText(notice.getBoardNo());
            notice.getBoard().setBoardText(contents);
        }
        model.addAttribute("notices", notices);
        return "home";
    }

    // 홈으로 가는 메소드(긴급공지 관련 로직O)
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        List<NoticeBoard> notices = NOTICEBOARDREPOSITORY.findAll(Sort.by(Sort.Direction.DESC, "board.view"));
        for (NoticeBoard notice : notices) {
            String contents = NOTICEBOARDSERVICE.modifyBoardText(notice.getBoardNo());
            notice.getBoard().setBoardText(contents);
        }
        model.addAttribute("notices", notices);
        return "home";
    }
}
