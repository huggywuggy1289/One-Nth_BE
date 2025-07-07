package com.onenth.OneNth.global.apiPayload.exception.handler;


import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}