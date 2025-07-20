package com.onenth.OneNth.global.apiPayload.code.status;

import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.code.ErrorReasonDTO;
import com.onenth.OneNth.global.apiPayload.exception.handler.SharingItemHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),

    // 회원 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_4001", "존재하지 않는 사용자입니다."),

    // 함께 나눠요 관련
    SHARING_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "SHARING_ITEM_4001", "존재하지 않는 품목입니다."),

    // 상품 리뷰 관련
    EXCEED_REVIEW_IMAGE_LIMIT(HttpStatus.BAD_REQUEST, "REVIEW_4001", "리뷰 이미지는 3장까지 업로드 가능합니다."),
    REVIEW_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_4002", "리뷰 내용을 입력해주세요."),
    REVIEW_RATE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "REVIEW_4003", "평점은 0 이상 5 이하이어야 합니다.");

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