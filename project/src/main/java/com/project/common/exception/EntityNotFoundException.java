package com.project.common.exception;

// 엔티티 조회 시 존재하지 않는 결과로 인한 예외 처리
public class EntityNotFoundException extends BusinessLogicException{

    private ErrorCode errorCode;

    public EntityNotFoundException(String value) {
        super(value, ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
