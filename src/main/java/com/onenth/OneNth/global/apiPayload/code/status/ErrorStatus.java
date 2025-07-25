package com.onenth.OneNth.global.apiPayload.code.status;

import com.onenth.OneNth.global.apiPayload.code.BaseErrorCode;
import com.onenth.OneNth.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "USER_4002", "금지된 요청입니다."),

    // 회원 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "존재하지 않는 사용자입니다."),

    // 함께 나눠요 관련
    SHARING_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "SHARING_ITEM_4001", "존재하지 않는 품목입니다."),

    // 같이 사요 관련
    PURCHASE_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "PURCHASE_ITEM_4001", "존재하지 않는 품목입니다."),

    // 상품 리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_4000", "존재하지 않는 리뷰입니다."),
    EXCEED_REVIEW_IMAGE_LIMIT(HttpStatus.BAD_REQUEST, "REVIEW_4001", "리뷰 이미지는 3장까지 업로드 가능합니다."),
    REVIEW_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_4002", "리뷰 내용을 입력해주세요."),
    REVIEW_RATE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "REVIEW_4003", "평점은 0.5 이상 5 이하이어야 합니다."),

    // 지역 관련
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "REGION4000", "존재하지 않는 지역입니다."),
    REGION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "REGION4001", "등록 가능한 지역은 최대 3개입니다."),
    REGION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "REGION4002", "이미 등록한 지역입니다."),
    MEMBER_REGION_NOT_FOUND(HttpStatus.BAD_REQUEST, "REGION4003", "해당 사용자가 등록하지 않은 지역입니다.");

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