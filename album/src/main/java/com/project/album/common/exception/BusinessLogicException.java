package com.project.album.common.exception;

public class BusinessLogicException extends RuntimeException{
    private ErrorCode errorCode;

    public BusinessLogicException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessLogicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
