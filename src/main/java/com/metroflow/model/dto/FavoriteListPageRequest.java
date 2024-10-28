package com.metroflow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteListPageRequest {
    private String userId;
    private int page;
    private int size;
}
