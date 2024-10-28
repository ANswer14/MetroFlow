package com.metroflow.controller;

import com.metroflow.model.dto.StationInfoResponse;
import com.metroflow.model.dto.SubwayMapInfo;
import com.metroflow.model.dto.User;
import com.metroflow.model.service.FavoriteListService;
import com.metroflow.model.service.SubwayMapService;
import com.metroflow.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StationInfoController {

    private final SubwayMapService subwayMapService;

    private final UserService USERSERVICE;

    private final FavoriteListService FavoriteListService;

    private static final Logger logger = LoggerFactory.getLogger(StationInfoController.class);

    @GetMapping("/station-info")
    public ResponseEntity<StationInfoResponse> getStationInfo(@RequestParam String stationName, Model model) {
        List<SubwayMapInfo> stationInfoList = subwayMapService.getSubwayMapInfo(stationName);
        logger.info("stationInfoList: {}", stationInfoList);

        User user = USERSERVICE.getUserObject();
        boolean isFavorite = false;
        List<Long> favoriteStationIdList = new ArrayList<>();

        // 즐겨찾기 등록 유무 판단을 위한 변수
        if( user != null ){
            System.out.println("유저 객체 정보 : " + user.getUserId());
            model.addAttribute("sessionUser", user);
            isFavorite = FavoriteListService.isFavorite(user.getUserId(), stationName);
            favoriteStationIdList = FavoriteListService.getFavoriteListStationIds(user.getUserId(), stationName);
            System.out.println("boolean value : " + isFavorite);
        }

        // StationInfoResponse 객체 생성
        StationInfoResponse response = new StationInfoResponse(stationInfoList, isFavorite, favoriteStationIdList);

        if (!stationInfoList.isEmpty()) {
            return ResponseEntity.ok(response); // 전체 리스트를 JSON 형태로 반환
        }
        return ResponseEntity.notFound().build();
    }
}
