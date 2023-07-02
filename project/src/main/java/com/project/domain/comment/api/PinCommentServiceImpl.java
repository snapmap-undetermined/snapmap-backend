package com.project.domain.comment.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.domain.comment.dto.PinCommentDTO;
import com.project.domain.comment.entity.PinComment;
import com.project.domain.comment.repository.PinCommentRepository;
import com.project.domain.pin.entity.Pin;
import com.project.domain.pin.repository.PinRepository;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PinCommentServiceImpl implements PinCommentService {

    private final PinRepository pinRepository;
    private final PinCommentRepository pinCommentRepository;

    @Override
    @Transactional
    public PinCommentDTO.PinCommentDetailResponse createPinComment(Users user, Long pinId, PinCommentDTO.CreatePinCommentRequest request) {

        Pin pin = pinRepository.findById(pinId).orElse(null);
        if (pin == null) {
            log.error("pin does not exist. pinId : {}", pinId);
        }

        PinComment pinComment = request.toEntity(user, pin);

        pinComment.setCommentOrder(pinCommentRepository.getLastPinCommentOrder(pin.getId()) + 1);

        // 부모 댓글은 부모 번호를 자신의 댓글 번호로 한다.
        if (pinComment.getParentCommentOrder() == null) {
            pinComment.setParentCommentOrder(pinComment.getCommentOrder());
        } else {
            long parentCommentOrder = pinComment.getParentCommentOrder();
            // 자식 댓글이라면, 부모 댓글의 자식 수를 증가시킨다.
            PinComment parentComm = pinCommentRepository.findByCommentOrder(parentCommentOrder);
            parentComm.plusChildCommentCount();

        }
        PinComment created = pinCommentRepository.save(pinComment);
        log.info("Pin comment created : {}", created);
        return new PinCommentDTO.PinCommentDetailResponse(pinComment);
    }

    @Override
    public List<PinCommentDTO.PinCommentDetailResponse> getPinCommentByPinId(Long pinId) {
        List<PinComment> pinCommentList = pinCommentRepository.findAllByPinId(pinId);
        log.info("Pin comments : {} by pinId : {}", pinCommentList, pinId);
        return pinCommentList.stream().map(PinCommentDTO.PinCommentDetailResponse::new).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void deletePinComment(Long pinCommentId) {

        PinComment pinComment = pinCommentRepository.findById(pinCommentId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 핀 댓글 입니다."));
        if (pinComment == null) {
            log.error("pinComment does not exist. pictureCommentId : {}", pinCommentId);
            throw new EntityNotFoundException("pinComment does not exist.");
        }

        PinComment parentPinComment;
        if (pinComment.getCommentOrder() == null) {
            log.error("No comment order at pinComment. pinCommentId : {}", pinCommentId);
            throw new BusinessLogicException("No comment order at pinComment", ErrorCode.ENTITY_NOT_FOUND);
        }

        if (!pinComment.getCommentOrder().equals(pinComment.getParentCommentOrder())) {
            // 부모 댓글 확인
            parentPinComment = pinCommentRepository.findByCommentOrder(pinComment.getParentCommentOrder());
            parentPinComment.minusChildCommentCount();
            // 자식 댓글이 없고, isDeleted = true인 부모 댓글은 삭제시킨다.
            if (parentPinComment.getChildCommentCount() == 0 && parentPinComment.getIsDeleted()) {
                log.info("Parent pin comment deleted. parentPinComment : {}", parentPinComment);
                pinCommentRepository.delete(parentPinComment);
            }
        }
        pinCommentRepository.delete(pinComment);
        log.info("Pin comment deleted. PinComment : {}", pinComment);
    }

    @Override
    public void deletePinCommentWithStatus(Long pinCommentId) {
        PinComment pinComment = pinCommentRepository.findById(pinCommentId).orElse(null);
        if (pinComment == null) {
            log.error("Pin comment does not exist. PinCommentId : {}", pinCommentId);
            throw new EntityNotFoundException("Pin comment does not exist.");
        }

        pinComment.setDeleted();
        log.info("Pin comment status to be deleted. PinCommentId : {}", pinCommentId);
    }

    @Override
    public PinCommentDTO.PinCommentDetailResponse updatePinComment(Long pinCommentId, PinCommentDTO.UpdatePinCommentRequest request) {
        PinComment pinComment = pinCommentRepository.findById(pinCommentId).orElse(null);
        if (pinComment == null) {
            log.error("Pin comment does not exist. PinCommentId : {}", pinCommentId);
            throw new EntityNotFoundException("Pin comment does not exist.");
        }

        if (request.getText() != null) {
            log.info("PinComment({}) text updated. {} -> {}", pinCommentId, pinComment.getText(), request.getText());
            pinComment.setText(request.getText());
        }
        return new PinCommentDTO.PinCommentDetailResponse(pinComment);
    }
}