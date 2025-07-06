package com.onenth.OneNth.apiPayload.exception.handler;

import com.example.UMC8th_MiniProject.apiPayload.code.BaseErrorCode;
import com.example.UMC8th_MiniProject.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}