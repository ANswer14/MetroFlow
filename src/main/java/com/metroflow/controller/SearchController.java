package com.metroflow.controller;


import com.metroflow.model.dto.SearchRequestDto;
import com.metroflow.model.dto.SearchResultInfo;
import com.metroflow.model.dto.SubwayStation;
import com.metroflow.model.service.SearchService;
import com.metroflow.model.service.UserService;
import com.metroflow.repository.SubwayStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final UserService USERSERVICE;
    private final SearchService searchService;

    @GetMapping("/goSearch")
    public String search(Model model) {
        model.addAttribute("sessionUser", USERSERVICE.getUserObject());
        return "search/search";
    }

    private final SubwayStationRepository subwayStationRepository;



    @ResponseBody
    @GetMapping("/goSearch/stations")
    public List<SubwayStation> getSubwayStations(String search) {
        return subwayStationRepository.findByStationNameContainingIgnoreCase(search);
    }

    @ResponseBody
    @GetMapping("/goSearch/stationLines")
    public List<SubwayStation> getStationLine(@RequestParam(value = "stationName", required = false) String stationName) {
        return subwayStationRepository.findStationLineByStationName(stationName);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @ResponseBody
    @PostMapping("/goSearch/result")
    public ResponseEntity<List<SearchResultInfo>> getSearchResult(@RequestBody SearchRequestDto requestDto) {

        // 데이터 처리
        List<SearchResultInfo> searchResults = searchService.getSearchResults(
                requestDto.getStationName(),
                requestDto.getStationLine(),
                requestDto.getAmpm(),
                requestDto.getHour(),
                requestDto.getMinute()
                );



        // 결과 페이지로 이동
        return ResponseEntity.ok(searchResults);
    }
}
