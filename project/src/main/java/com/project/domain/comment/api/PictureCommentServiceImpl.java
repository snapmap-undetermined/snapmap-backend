package com.project.domain.comment.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.comment.entity.PictureComment;
import com.project.domain.comment.repository.PictureCommentRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PictureCommentServiceImpl implements PictureCommentService {

    private final PictureRepository pictureRepository;
    private final PictureCommentRepository pictureCommentRepository;

    @Override
    public PictureCommentDTO.PictureCommentDetailResponse createPictureComment(Users user, Long pictureId, PictureCommentDTO.CreatePictureCommentRequest request) {

        Picture picture = pictureRepository.findById(pictureId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사진 입니다."));
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
        pictureCommentRepository.save(pictureComment);

        return new PictureCommentDTO.PictureCommentDetailResponse(pictureComment);
    }

    @Override
    public List<PictureCommentDTO.PictureCommentDetailResponse> getPictureCommentByPictureId(Long pictureId) {

        List<PictureComment> pictureCommentList = pictureCommentRepository.findAllByPictureId(pictureId);

        return pictureCommentList.stream().map(PictureCommentDTO.PictureCommentDetailResponse::new).collect(Collectors.toList());

    }

    @Override
    public void deletePictureComment(Long pictureCommentId) {

        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사진 댓글 입니다."));
        PictureComment parentPictureComment;
        if (!pictureComment.getCommentOrder().equals(pictureComment.getParentCommentOrder())) {
            // 부모 댓글 확인
            parentPictureComment = pictureCommentRepository.findByCommentOrder(pictureComment.getParentCommentOrder());
            parentPictureComment.minusChildCommentCount();
            // 자식 댓글이 없고, isDeleted = true인 부모 댓글은 삭제시킨다.
            if (parentPictureComment.getChildCommentCount() == 0 && parentPictureComment.getIsDeleted())
                pictureCommentRepository.delete(parentPictureComment);

        }
        pictureCommentRepository.delete(pictureComment);

    }

    @Override
    public void deletePictureCommentWithStatus(Long pictureCommentId) {

        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElseThrow(
                () -> new EntityNotFoundException("해당 사진 댓글이 존재하지 않습니다."));

        pictureComment.setDeleted();

    }

    @Override
    @Transactional
    public PictureCommentDTO.PictureCommentDetailResponse updatePictureComment(Long pictureCommentId, PictureCommentDTO.UpdatePictureCommentRequest request) {
        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElseThrow(
                () -> new EntityNotFoundException("해당 사진 댓글이 존재하지 않습니다."));
        pictureComment.setText(request.getText());

        return new PictureCommentDTO.PictureCommentDetailResponse(pictureComment);
    }
}
