package com.project.album.common.exception;

// 엔티티 조회 시 존재하지 않는 결과로 인한 예외 처리
public class EntityNotFoundException extends BusinessLogicException{

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
