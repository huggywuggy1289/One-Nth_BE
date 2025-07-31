package com.onenth.OneNth.global.apiPayload.exception.handler;

import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

public class ChatHandler extends GeneralException {
    public ChatHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}