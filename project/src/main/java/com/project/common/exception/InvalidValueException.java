package com.project.common.exception;

// 유효하지 않은 값에 대한 예외 처리
public class InvalidValueException extends BusinessLogicException {

    public InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
