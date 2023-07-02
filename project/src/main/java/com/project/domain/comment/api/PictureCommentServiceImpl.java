package com.project.domain.comment.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.comment.entity.PictureComment;
import com.project.domain.comment.repository.PictureCommentRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
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
public class PictureCommentServiceImpl implements PictureCommentService {

    private final PictureRepository pictureRepository;
    private final PictureCommentRepository pictureCommentRepository;

    @Override
    public PictureCommentDTO.PictureCommentDetailResponse createPictureComment(Users user, Long pictureId, PictureCommentDTO.CreatePictureCommentRequest request) {
        Picture picture = pictureRepository.findById(pictureId).orElse(null);
        if (picture == null) {
            log.error("Picture does not exist. pictureId : {}", pictureId);
        }

        PictureComment pictureComment = request.toEntity(user, picture);

        pictureComment.setCommentOrder(pictureCommentRepository.getLastPictureCommentOrder(picture.getId()) + 1);
        long parentCommentOrder;
        // 부모 댓글은 부모 번호를 자신의 댓글 번호로 한다.
        if (pictureComment.getParentCommentOrder() == null) {
            pictureComment.setParentCommentOrder(pictureComment.getCommentOrder());
        } else {
            // 자식 댓글이라면, 부모 댓글의 자식 수를 증가시킨다.
            parentCommentOrder = pictureComment.getParentCommentOrder();
            PictureComment parentComm = pictureCommentRepository.findByCommentOrder(parentCommentOrder);
            parentComm.plusChildCommentCount();
        }
        PictureComment created = pictureCommentRepository.save(pictureComment);
        log.info("Picture comment created : {}", created);

        return new PictureCommentDTO.PictureCommentDetailResponse(pictureComment);
    }

    @Override
    public List<PictureCommentDTO.PictureCommentDetailResponse> getPictureCommentByPictureId(Long pictureId) {
        List<PictureComment> pictureCommentList = pictureCommentRepository.findAllByPictureId(pictureId);
        log.info("Picture comments : {} by pictureId : {}", pictureCommentList, pictureId);
        return pictureCommentList.stream().map(PictureCommentDTO.PictureCommentDetailResponse::new).collect(Collectors.toList());

    }

    @Override
    public void deletePictureComment(Long pictureCommentId) {
        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElse(null);
        if (pictureComment == null) {
            log.error("PictureComment does not exist. pictureCommentId : {}", pictureCommentId);
            throw new EntityNotFoundException("PictureComment does not exist.");
        }

        PictureComment parentPictureComment;
        if (pictureComment.getCommentOrder() == null) {
            log.error("No comment order at pictureComment. pictureCommentId : {}", pictureCommentId);
            throw new BusinessLogicException("No comment order at pictureComment", ErrorCode.ENTITY_NOT_FOUND);
        }

        if (!pictureComment.getCommentOrder().equals(pictureComment.getParentCommentOrder())) {
            // 부모 댓글 확인
            parentPictureComment = pictureCommentRepository.findByCommentOrder(pictureComment.getParentCommentOrder());
            parentPictureComment.minusChildCommentCount();
            // 자식 댓글이 없고, isDeleted = true인 부모 댓글은 삭제시킨다.
            if (parentPictureComment.getChildCommentCount() == 0 && parentPictureComment.getIsDeleted()){
                log.info("Parent picture comment deleted. parentPictureComment : {}", parentPictureComment);
                pictureCommentRepository.delete(parentPictureComment);
            }
        }
        pictureCommentRepository.delete(pictureComment);
        log.info("Picture comment deleted. pictureComment : {}", pictureComment);
    }

    @Override
    public void deletePictureCommentWithStatus(Long pictureCommentId) {
        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElse(null);
        if (pictureComment == null) {
            log.error("Picture comment does not exist. pictureCommentId : {}", pictureCommentId);
            throw new EntityNotFoundException("Picture comment does not exist.");
        }

        pictureComment.setDeleted();
        log.info("Picture comment status to be deleted. pictureCommentId : {}", pictureCommentId);
    }

    @Override
    @Transactional
    public PictureCommentDTO.PictureCommentDetailResponse updatePictureComment(Long pictureCommentId, PictureCommentDTO.UpdatePictureCommentRequest request) {
        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElse(null);
        if (pictureComment == null) {
            log.error("Picture comment does not exist. pictureCommentId : {}", pictureCommentId);
            throw new EntityNotFoundException("Picture comment does not exist.");
        }

        if (request.getText() != null) {
            log.info("PictureComment({}) text updated. {} -> {}", pictureCommentId, pictureComment.getText(), request.getText());
            pictureComment.setText(request.getText());
        }

        return new PictureCommentDTO.PictureCommentDetailResponse(pictureComment);
    }
}
