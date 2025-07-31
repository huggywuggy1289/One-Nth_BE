package com.onenth.OneNth.global.external.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeoCodingResult {
    private double latitude;       // 위도
    private double longitude;      // 경도
    private String regionName;     // 예: "필동3가"
}
