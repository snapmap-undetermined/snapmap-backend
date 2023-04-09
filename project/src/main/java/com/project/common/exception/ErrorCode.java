package com.project.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonFormat(shape = JsonFormat.Shape.OBJECT) // Serialize
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "요청값이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(405, "C002", "존재하지 않는 HTTP 요청입니다."),
    ENTITY_NOT_FOUND(404, "C003", "존재하지 않는 값입니다."),
    INTERNAL_SERVER_ERROR(500, "C004", "알 수 없는 오류가 발생했습니다."),
    INVALID_TYPE_VALUE(400, "C005", "요청값의 타입이 유효하지 않습니다."),
    ACCESS_DENIED(403, "C006", "접근 권한이 없습니다."),

    IMAGE_PROCESSING_ERROR(500, "I007", "이미지 파일 처리에 실패했습니다."),

    // Member
    EMAIL_DUPLICATION(400, "M001", "이미 가입된 이메일입니다."),
    NICKNAME_DUPLICATION(400, "M002", "이미 존재하는 닉네임입니다."),
    LOGIN_INPUT_INVALID(400, "M003", "이메일 혹은 비밀번호를 확인해주세요."),

    // Circle
    CIRCLE_MANAGER_ERROR(400, "C002", "그룹의 방장 관련 오류가 발생했습니다."),

    // Friend
    FRIEND_DUPLICATION(400,"F001","이미 존재하는 친구 관계 입니다."),
    ;

    private final String code;
    private final String message;
    private final int status;


    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
