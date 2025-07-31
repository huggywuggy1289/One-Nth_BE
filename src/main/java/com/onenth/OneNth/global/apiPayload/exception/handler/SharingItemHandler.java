package com.onenth.OneNth.global.apiPayload.exception.handler;

import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

public class SharingItemHandler extends GeneralException {
    public SharingItemHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
