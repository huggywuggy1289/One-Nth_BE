package com.onenth.OneNth.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private boolean isSuccess;
    private String code;
    private String message;
    private T data;
}
