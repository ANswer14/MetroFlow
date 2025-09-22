package com.metroflow.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @ResponseBody에 넣을 폼(recommendation 컨트롤러 속)
public class RecommendationRequestForm {
    private Long boardNo;
    @JsonProperty("isThumbsUp")
    private boolean isThumbsUp;
    @JsonProperty("isThumbsDown")
    private boolean isThumbsDown;
    @JsonProperty("priorThumbsUp")
    private boolean priorThumbsUp;
    @JsonProperty("priorThumbsDown")
    private boolean priorThumbsDown;
}
