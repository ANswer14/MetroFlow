package com.metroflow.controller;

import com.metroflow.model.dto.*;
import com.metroflow.model.service.*;
import com.metroflow.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FavoriteListController {

    private final FavoriteListService favoriteListService;
    private final UserService userService;
    private final SubwayMapService subwayMapService;
    private final IsHolidaysService isHolidaysService;
    private final SearchService searchService;
    private final NoticeBoardRepository NOTICEBOARDREPOSITORY;
    private final NoticeBoardService NOTICEBOARDSERVICE;

    @PostMapping("/addToFavorite")
        public ResponseEntity<FavoriteApiResponse<String>> addToFavorite(@RequestBody FavoriteRequest request) {
        String request_user_id = userService.getUserObject().getUserId();
        long request_station_id= request.getStation_id();

        System.out.println("즐겨찾기 등록 대상 유저 ID : "+request_user_id);
        System.out.println("대상 역 id : "+request_station_id);

        // request_user_id가 null이 아닌 경우에만 실행
        if (request_user_id != null) {
            favoriteListService.addToFavorites(request_user_id, request_station_id);
            return ResponseEntity.ok(new FavoriteApiResponse<>("즐겨찾기 추가 완료", null, true));
        } else {
            return ResponseEntity.badRequest().body(new FavoriteApiResponse<>("유저 ID가 null입니다.", null, false));
        }

    }

    @PostMapping("/deleteFromFavoriteList")
    public ResponseEntity<FavoriteApiResponse<String>> deleteFromFavoriteList(@RequestBody FavoriteRequest request){
        String request_user_id = userService.getUserObject().getUserId();
        long request_station_id= request.getStation_id();

        System.out.println("즐겨찾기 삭제 대상 유저 ID : "+request_user_id);
        System.out.println("삭제 대상 역 id : "+request_station_id);

        // request_user_id가 null이 아닌 경우에만 실행
        if (request_user_id != null) {
            favoriteListService.deleteFromFavorites(request_user_id, request_station_id);
            return ResponseEntity.ok(new FavoriteApiResponse<>("즐겨찾기 삭제 완료", null, true));
        } else {
            return ResponseEntity.badRequest().body(new FavoriteApiResponse<>("유저 ID가 null입니다.", null, false));
        }
    }

    // 메뉴바에서 a 태그 버튼 눌렀을 경우
    @GetMapping("/goFavoriteList")
    public String goFavoriteList(@RequestParam(defaultValue = "0") int page, Model model){

        String favoriteList_user_id = userService.getUserObject().getUserId();

        if(favoriteList_user_id != null){
            // 4개 레코드가 완전한 데이터 1개 !!
            // station_id 하나에 대해 요일 구분(평일, 휴일) / 방향 구분(상선, 하선 or 외선, 내선)
            // 즉, 2 x 2 = 4가지의 데이터가 쿼리 결과로 넘어온다.
            // 그래서 size가 4 단위여야 1개의 station_id 에 대한 온전한 데이터 전체를 받아 올 수 있음
            int size = 32;
            Pageable pageable = PageRequest.of(page, size);
            Page<FavoriteListPageDto> favoriteListPage = favoriteListService.getFavoriteListByUserId(favoriteList_user_id,pageable);

            // setTime을 인덱스처럼 사용할 예정 (h0530 같은 값으로)
            String setTime = subwayMapService.isValidTimeFormat(subwayMapService.getCurrentTimeColumn());
            String dayType = subwayMapService.getCurrentDayType();

            // ampm, 시, 분 을 가져오는 기능
            // 페이지 최초 접근시에는 현재 기준으로 설정
            TimeChecker.ClockDTO timeAttributes = isHolidaysService.getCurrentTimeAttributes();
            model.addAttribute("ampm",timeAttributes.getAmpm());
            model.addAttribute("hour",timeAttributes.getHour());
            model.addAttribute("minute",timeAttributes.getMinute());

            // 현재 시간 기준 연, 월, 일을 어트리뷰트로 추가
            String year = String.valueOf(LocalDate.now().getYear());
            String month = String.format("%02d", LocalDate.now().getMonthValue());
            String day = String.format("%02d", LocalDate.now().getDayOfMonth());
            model.addAttribute("year",year);
            model.addAttribute("month",month);
            model.addAttribute("day",day);

            // station_id를 이용해 매핑 처리해서 station_id 하위 항목으로 상선, 하선 데이터가 들어가도록 조정
            // 그룹화 (평일, 휴일 기준으로 매칭되는 데이터만 매핑시킴)
            Map<Long, List<FavoriteListPageDto>> raw_groupedFavorites = favoriteListPage.getContent().stream()
                    .filter(item -> item.getDayType().equals(dayType)) // dayType이 일치하는 항목만 필터링
                    .collect(Collectors.groupingBy(FavoriteListPageDto::getStationId));

            // station_id 순으로 정렬(오름차순)
            Map<Long, List<FavoriteListPageDto>> groupedFavorites = new TreeMap<>(raw_groupedFavorites);
            
            // Map 확인용 출력
//            groupedFavorites.forEach((stationId, favorites) -> {
//                System.out.println("Station ID: " + stationId);
//                favorites.forEach(favorite -> {
//                    System.out.println("  getStationLine: " + favorite.getStationLine());
//                    System.out.println("  getStationId: " + favorite.getStationId());
//                    System.out.println("  getStationName: " + favorite.getStationName());
//                    System.out.println("  getDayType: " + favorite.getDayType());
//                    System.out.println("  getDirectionType: " + favorite.getDirectionType());
//                    System.out.println();
//                });
//            });

            // 모델에 데이터 추가
            model.addAttribute("setTime", setTime); // 현재 시간 기준 인덱스(h0000 형태)
            model.addAttribute("groupedFavorites", groupedFavorites);   // 매핑시킨 데이터
            model.addAttribute("favoriteList", favoriteListPage.getContent()); // 페이지의 내용
            model.addAttribute("totalPages", favoriteListPage.getTotalPages()); // 총 페이지 수
            model.addAttribute("currentPage", favoriteListPage.getNumber()); // 현재 페이지 번호
        }

        model.addAttribute("sessionUser", userService.getUserObject());

        List<NoticeBoard> notices = NOTICEBOARDREPOSITORY.findAll(Sort.by(Sort.Direction.DESC, "boardNo"));
        for (NoticeBoard notice : notices) {
            String contents = NOTICEBOARDSERVICE.modifyBoardText(notice.getBoardNo());
            notice.getBoard().setBoardText(contents);
        }
        model.addAttribute("notices", notices);


        return "favoriteList/favoriteList";
    }

    @GetMapping("/goFavoriteListWithSetTime")
    public String goFavoriteListWithSetTime(@RequestParam(required = false) String year,
                                            @RequestParam(required = false) String month,
                                            @RequestParam(required = false) String day,
                                            @RequestParam(required = false) String ampm,
                                            @RequestParam(required = false) String hour,
                                            @RequestParam(required = false) String minute,
                                            @RequestParam(defaultValue = "0") int page,
                                            Model model){

        // 날짜 미 선택시 현재 날짜 기준 표시, 선택한 경우는 포맷팅 처리
        TimeChecker.CanlendarDTO calendarChecker = favoriteListService.calendarChecker(year,month,day);
        year = calendarChecker.getYear();
        month = calendarChecker.getMonth();
        day = calendarChecker.getDay();

        // 시간 미선택 시 현재 시간 기준 처리
        TimeChecker.ClockDTO clockChecker = favoriteListService.clockChecker(ampm, hour, minute);
        ampm = clockChecker.getAmpm();
        hour = clockChecker.getHour();
        minute = clockChecker.getMinute();

        // dayType 분류를 위한 변수
        String dayType = isHolidaysService.classifyDate(LocalDate.parse(year + "-" + month + "-" + day));
        
        // 즐겨찾기 목록을 위해 세션에서 userId 가져오기
        String favoriteListUserId = userService.getUserObject().getUserId();

        if(favoriteListUserId != null){
            // 4개 레코드가 완전한 데이터 1개 !!
            // station_id 하나에 대해 요일 구분(평일, 휴일) / 방향 구분(상선, 하선 or 외선, 내선)
            // 즉, 2 x 2 = 4가지의 데이터가 쿼리 결과로 넘어온다.
            // 그래서 size가 4단위여야 1개의 station_id 에 대한 온전한 데이터 전체를 받아 올 수 있음
            int size = 32;
            Pageable pageable = PageRequest.of(page, size);
            Page<FavoriteListPageDto> favoriteListPage = favoriteListService.getFavoriteListByUserId(favoriteListUserId,pageable);

            // setTime을 인덱스처럼 사용할 예정 (h0530 같은 값으로)
            // 사용자가 설정한 시간으로 바꿔줘야함
            // 정확한 시간 포맷인지 검사후 넘기기(isValidTimeFormat)
            String setTime = subwayMapService.isValidTimeFormat(searchService.getInputColumn(ampm, hour, minute));

            // ampm, 시, 분 을 어트리뷰트로 추가
            model.addAttribute("ampm",ampm);
            model.addAttribute("hour",hour);
            model.addAttribute("minute",minute);

            // 연,월,일 어트리뷰트로 추가
            model.addAttribute("year",year);
            model.addAttribute("month",month);
            model.addAttribute("day",day);

            // station_id를 이용해 매핑 처리해서 station_id 하위 항목으로 상선, 하선 데이터가 들어가도록 조정
            // 그룹화 (평일, 휴일 기준으로 매칭되는 데이터만 매핑시킴)
            Map<Long, List<FavoriteListPageDto>> raw_groupedFavorites = favoriteListPage.getContent().stream()
                    .filter(item -> item.getDayType().equals(dayType)) // dayType이 일치하는 항목만 필터링
                    .collect(Collectors.groupingBy(FavoriteListPageDto::getStationId));

            // station_id 순으로 정렬(오름차순)
            Map<Long, List<FavoriteListPageDto>> groupedFavorites = new TreeMap<>(raw_groupedFavorites);

            // 모델에 데이터 추가
            model.addAttribute("setTime", setTime); // 사용자 요구 시간 기준 인덱스(h0000 형태)
            model.addAttribute("groupedFavorites", groupedFavorites);   // 매핑시킨 데이터
            model.addAttribute("favoriteList", favoriteListPage.getContent()); // 페이지의 내용
            model.addAttribute("totalPages", favoriteListPage.getTotalPages()); // 총 페이지 수
            model.addAttribute("currentPage", favoriteListPage.getNumber()); // 현재 페이지 번호

        }

        model.addAttribute("sessionUser", userService.getUserObject());

        List<NoticeBoard> notices = NOTICEBOARDREPOSITORY.findAll(Sort.by(Sort.Direction.DESC, "boardNo"));
        for (NoticeBoard notice : notices) {
            String contents = NOTICEBOARDSERVICE.modifyBoardText(notice.getBoardNo());
            notice.getBoard().setBoardText(contents);
        }
        model.addAttribute("notices", notices);


        return "favoriteList/favoriteList";


    }

}
