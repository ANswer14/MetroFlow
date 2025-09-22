package com.metroflow.model.service;

import com.metroflow.model.dto.User;
import com.metroflow.model.dto.UserForm;
import com.metroflow.model.dto.UserRegisterForm;
import com.metroflow.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository USERREPOSITORY;
    private final BCryptPasswordEncoder BCRYPTPASSWORDENCODER;

    // ID 중복 체크
    public void idDuplicationCheck(UserRegisterForm user, BindingResult result) {
        if (USERREPOSITORY.findByUserId(user.getUserId()).isPresent()) {
            result.rejectValue("userId", "duplication", "id 중복");
        }
    }

    // 비밀번호 확인
    public void passwordCheck(@Valid UserRegisterForm user, BindingResult result) {
        // null 체크 추가
        if (user.getPassword() == null || user.getPasswordCheck() == null) {
            result.rejectValue("passwordCheck", "null", "비밀번호가 입력되지 않았습니다.");
            return;
        }

        if (!user.getPassword().equals(user.getPasswordCheck())) {
            result.rejectValue("passwordCheck", "notequal", "비밀번호가 다릅니다.");
        }

    }

    // 닉네임 중복 확인
    public void nicknameDuplicationCheck(UserRegisterForm user, BindingResult result) {
        if (USERREPOSITORY.findByNickname(user.getNickname()).isPresent()) {
            result.rejectValue("nickname", "duplication", "닉네임 중복");
        }
    }

    // 현재 세션의 유저 객체 model에 저장하는 메소드
    public User getUserObject() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        System.out.println("auth : " + auth);
        if (auth != null) {
            String userId = auth.getName(); // 로그인한 사용자 id
            Optional<User> optionalUser = USERREPOSITORY.findByUserId(userId);
            if (optionalUser.isPresent()) {
                user = optionalUser.get(); // 사용자가 존재할 경우 User 객체 가져오기
            } else {
                // 사용자가 존재하지 않을 경우 처리 (예: null 반환 또는 예외 발생)
                System.out.println("User not found for userId: " + userId);
            }
        }
        return user;
    }

    // 유저 리스트 용 페이징 처리 로직
    @Transactional(readOnly = true)
    public Page<UserForm> allUserPaging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // 인덱스 값이라 보일 값보다 -1 해줘야함
        int pageLimit = 8; // 한 페이지에 보여줄 글 갯수
        // 한 페이지당 8개씩 글을 보여주고 정렬 기준은 userId 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        // 모든 보드들을 페이징 처리
        Page<User> users =
                USERREPOSITORY.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.ASC, "userId")));
        return users.map(user -> new UserForm(user.getUserId(), user.getNickname(), user.getUserRole()));
    }


    // 사용자 정보 업데이트
    public void updateUser(User user, String currentPassword, String newPassword
            , String confirmPassword, String nickname, String email, String ProfilePic) throws IllegalArgumentException{
        System.out.println("pw : "+ currentPassword);

        // 현재 비밀 번호 확인
        if (!BCRYPTPASSWORDENCODER.matches(currentPassword, user.getPassword())) {
            System.out.println("wrong prior password");
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 일치 여부 확인
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("wrong confirm password");
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }

        // 닉네임 중복 확인
        Optional<User> nicknameUser = USERREPOSITORY.findByNickname(nickname);
        if (nicknameUser.isPresent() && !nicknameUser.get().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 변경시 비밀번호 업데이트
        if (!newPassword.isEmpty()) {
            user.setPassword(BCRYPTPASSWORDENCODER.encode(newPassword));
        }

        // 이메일, 닉네임 변경시 이메일 업데이트
        user.setUserEmail(email);
        user.setNickname(nickname);

        // 프로필 이미지 업데이트
        user.setUserImgPath(ProfilePic);

        // 변경된 사용자 정보 저장
        USERREPOSITORY.save(user);

    }

    // 유효성 검사 및 비밀번호 확인 로직을 처리하는 메소드
    public String validateUserProfile(UserRegisterForm user, BindingResult result) {

        // 유효성 검사에서 에러가 발생한 경우 처리
        if (result.hasErrors()) {
            return "입력한 정보에서 오류가 발생했습니다.";
        }

        // 추가적인 비밀번호 확인 로직
        if (!user.getPassword().equals(user.getPasswordCheck())) {
            return "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.";
        }

        return null; // 에러가 없을 경우
    }

}