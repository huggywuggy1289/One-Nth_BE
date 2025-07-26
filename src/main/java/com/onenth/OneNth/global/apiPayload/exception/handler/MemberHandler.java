package com.onenth.OneNth.global.apiPayload.exception.handler;

import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {
  public MemberHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
