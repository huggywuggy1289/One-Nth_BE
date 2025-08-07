package com.onenth.OneNth.domain.map.enums;

import com.onenth.OneNth.domain.post.entity.enums.PostType;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

import java.util.Arrays;

public enum MarkerType {

    PURCHASEITEM("purchase-item"),
    SHARINGITEM("sharing-item"),
    DISCOUNT("discount", PostType.DISCOUNT),
    RESTAURANT("restaurant", PostType.RESTAURANT);

    private final String value;
    private final PostType postType;

    MarkerType(String value, PostType postType) {
        this.value = value;
        this.postType = postType;
    }

    MarkerType(String value) {
        this(value, null);
    }

    public PostType getPostType() {
        if (postType == null) {
            throw new GeneralException(ErrorStatus.INVALID_MARKER_TYPE);
        }
        return postType;
    }

    public static MarkerType from(String value) {
        return Arrays.stream(MarkerType.values())
                .filter(m -> m.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MARKER_TYPE));
    }
}
