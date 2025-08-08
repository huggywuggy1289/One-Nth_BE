package com.onenth.OneNth.global.apiPayload.exception.handler;

import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

public class DealHandler extends GeneralException {
    public DealHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
