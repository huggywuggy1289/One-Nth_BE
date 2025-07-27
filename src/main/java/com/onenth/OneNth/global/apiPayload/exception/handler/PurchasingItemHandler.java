package com.onenth.OneNth.global.apiPayload.exception.handler;

import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

public class PurchasingItemHandler extends GeneralException {
    public PurchasingItemHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
