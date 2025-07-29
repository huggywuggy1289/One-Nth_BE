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
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON403", "입력값이 유효하지 않습니다."),

    // 회원 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER001", "존재하지 않는 사용자입니다."),

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
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "REGION001", "존재하지 않는 지역입니다."),
    REGION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMBER_REGION001", "등록 가능한 지역은 최대 3개입니다."),
    REGION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER_REGION002", "이미 등록한 지역입니다."),
    MEMBER_REGION_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER_REGION003", "해당 사용자가 등록하지 않은 지역입니다."),

    // 지역 키워드 알림 관련
    REGION_KEYWORD_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "KEYWORD_ALERT001", "등록 가능한 지역 알림은 최대 3개입니다."),
    REGION_KEYWORD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "KEYWORD_ALERT002", "이미 알림으로 등록한 지역입니다."),
    REGION_KEYWORD_NOT_FOUND_OR_NOT_YOURS(HttpStatus.BAD_REQUEST, "KEYWORD_ALERT003", "해당 지역 키워드 알림이 존재하지 않거나 접근 권한이 없습니다"),

    // 상품 키워드 알림 관련
    PRODUCT_KEYWORD_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "KEYWORD_ALERT004", "등록 가능한 키워드 알림은 최대 5개입니다."),
    PRODUCT_KEYWORD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "KEYWORD_ALERT005", "이미 알림으로 등록한 키워드입니다."),
    PRODUCT_KEYWORD_NOT_FOUND_OR_NOT_YOURS(HttpStatus.BAD_REQUEST, "KEYWORD_ALERT006", "해당 키워드 알림이 존재하지 않거나 접근 권한이 없습니다"),

    // 채팅 관련
    CHAT_ROOM_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "CHAT_ROOM001", "자기 자신과의 채팅방 생성은 불가능합니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHAT_ROOM002", "채팅방이 존재하지 않습니다."),

    //게시글 관련
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "POST_001", "게시글을 찾을 수 없습니다."),

    // 알림 관련
    ALERT_SETTING_NOT_FOUND(HttpStatus.BAD_REQUEST, "ALERT_SETTING001", "해당 사용자의 알림 설정 정보가 존재하지 않습니다."),
    UNEXPECTED_ALERT_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "ALERT_002", "지원하지 않는 AlertType입니다."),

    // 프로필 이미지 관련
    INVALID_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "PROFILE001", "프로필 이미지가 비어 있거나 존재하지 않습니다."),
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