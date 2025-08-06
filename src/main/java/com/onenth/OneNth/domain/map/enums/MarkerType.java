package com.onenth.OneNth.domain.map.enums;

import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Marker;

import java.util.Arrays;

@RequiredArgsConstructor
public enum MarkerType {
    SHARINGITEM("purchase-item"),
    PURCHASEITEM("sharing-item"),
    DISCOUNT("discount"),
    RESTAURANT("restaurant");

    private final String value;

    public static MarkerType from(String value) {
        return Arrays.stream(MarkerType.values())
                .filter(m -> m.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MARKER_TYPE));
    }

    public String getValue() {
        return value;
    }
}
