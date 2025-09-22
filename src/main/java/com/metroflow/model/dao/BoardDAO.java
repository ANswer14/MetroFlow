package com.metroflow.model.dao;

import com.metroflow.model.dto.Board;
import com.metroflow.model.dto.Recommendation;
import com.metroflow.model.dto.User;
import com.metroflow.model.service.BoardService;
import com.metroflow.model.service.UserService;
import com.metroflow.repository.BoardRepository;
import com.metroflow.repository.RecommendationRepository;
import com.metroflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardDAO {

    private final BoardRepository BOARDREPOSITORY;
    private final BoardService BOARDSERVICE;
    private final UserRepository USERREPOSITORY;
    private final UserService USERSERVICE;
    private final RecommendationRepository RECOMMENDATIONREPOSITORY;

    // 보드 인스턴스를 추가하는 메소드(Insert)
    public void writeBoard(Board board) {
//        System.out.println("writeBoard 호출됨");
        board.setUser(USERREPOSITORY.findByUserId(board.getUser().getUserId()).get()); // 보드 엔티티에 있는 User는 userId와 매핑되어있어서 userId의 값만 저장됨 => 그 userId를 통해 User 객체를 찾아야함
        board.setCreatedTime(LocalDateTime.now()); // 현재 시간 설정
        board.setThumbsUp(0L); // 보드의 좋아요 수
        board.setThumbsDown(0L); // 보드의 싫어요 수
        board.setView(0L); // 조회수
        board.setNoticeBoard(false); // 긴급 공지인지 T / F
        BOARDREPOSITORY.save(board); // Insert
    }

    // Recommendation 인스턴스를 추가하는 메소드(Insert)
    public void insertRecommendation(Long no) {
        Board board = BOARDSERVICE.getBoardInfo(no); // 해당 게시물의 보드 정보
        User user = USERSERVICE.getUserObject(); // 현재 세션 유저 객체
        if (RECOMMENDATIONREPOSITORY.findRecommendationByUserAndBoard(user.getUserId(), no).isPresent()) { // userId와 boardNo를 통해 해당 Recommendation 객체가 있으면 true, 없으면 false
            return;
        } else { // 없다면 객체 만들기
            Recommendation rec = new Recommendation();
            rec.setUser(user); // 유저 객체
            rec.setBoard(board); // 보드 객체
            rec.setThumbsUp(false); // 유저의 해당 보드에 대한 좋아요 여부
            rec.setThumbsDown(false); // 유저의 해당 보드에 대한 싫어요 여부
            RECOMMENDATIONREPOSITORY.save(rec);
        }
    }

    // 파라미터로 받아온 내용에 따라 좋아요 수 변동을 결정해주는 로직
    public void updateRecommendation(Long no, boolean up, boolean down, boolean priorUp, boolean priorDown) {
        String userId = USERSERVICE.getUserObject().getUserId();
        RECOMMENDATIONREPOSITORY.updateRecommendationByUserAndBoard(up, down, userId, no);
        if (up && priorUp) { // up이 true이고 이전Up(priorUp)이 true이면 누르지 않은것이므로 void return
            return;
        } else if (up && priorDown) { // up이 true이고 이전Down(priorDown)이 true이면 down이 눌려져있다가 up을 누른것이므로 up + 1, down - 1
            BOARDREPOSITORY.updateThumbs(1, -1, no);
        } else if (down && priorDown) { // down이 true이고 이전Down(priorDown)이 true이면 아무것도 누르지 않은것이므로 void return
            return;
        } else if (down && priorUp) { // down이 true이고 이전Up(priorUp)이 true이면 up이 눌려져있다가 down을 누른것이므로 down + 1, up - 1
            BOARDREPOSITORY.updateThumbs(-1, 1, no);
        } else if (up) { // up이 true이고, 이전Down(priorDown), 이전Up(priorUp)이 false이면 up만 누른것이므로 up + 1
            BOARDREPOSITORY.updateThumbs(1, 0, no);
        } else if (down) { // down이 true이고, 이전Down(priorDown), 이전Up(priorUp)이 false이면 down만 누른것이므로 down + 1
            BOARDREPOSITORY.updateThumbs(0, 1, no);
        } else if (priorUp) { // up, down이 false이고, 이전Up(priorUp) true이면 up이 눌려져있다가 아무것도 안누른것으로 바꾼것으로 up - 1
            BOARDREPOSITORY.updateThumbs(-1, 0, no);
        } else if (priorDown) { // up, down이 false이고, 이전Down(priorDown) true이면 down이 눌려져있다가 아무것도 안누른것으로 바꾼것으로 down - 1
            BOARDREPOSITORY.updateThumbs(0, -1, no);
        }
    }

}
