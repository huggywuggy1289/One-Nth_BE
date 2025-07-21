package com.onenth.OneNth.global.apiPayload.code.status;

import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER001", "존재하지 않는 사용자입니다."),
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "REGION001", "존재하지 않는 지역입니다."),

    REGION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMBER_REGION001", "등록 가능한 지역은 최대 3개입니다."),
    REGION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER_REGION002", "이미 등록한 지역입니다."),
    MEMBER_REGION_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER_REGION003", "해당 사용자가 등록하지 않은 지역입니다."),

    REGION_KEYWORD_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "REGION_ALERT001", "등록 가능한 지역 알림은 최대 3개입니다."),
    REGION_KEYWORD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "REGION_ALERT002", "이미 알림으로 등록한 지역입니다."),
    REGION_KEYWORD_NOT_FOUND_OR_NOT_YOURS(HttpStatus.BAD_REQUEST, "REGION_ALERT003", "해당 지역 키워드 알림이 존재하지 않거나 접근 권한이 없습니다"),

    KEYWORD_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "KEYWORD_ALERT001", "등록 가능한 키워드 알림은 최대 5개입니다."),
    KEYWORD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "KEYWORD_ALERT002", "이미 알림으로 등록한 키워드입니다."),

    ;
    
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}