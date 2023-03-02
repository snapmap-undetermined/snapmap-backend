package com.project.domain.comment.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.comment.entity.PictureComment;
import com.project.domain.comment.repository.PictureCommentRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PictureCommentServiceImpl implements PictureCommentService {

    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final PictureCommentRepository pictureCommentRepository;

    @Override
    public PictureCommentDTO.PictureCommentDetailResponse createPictureComment(Users user, PictureCommentDTO.CreatePictureCommentRequest request) {

        Users getUser = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
        Picture getPicture = pictureRepository.findById(request.getPictureId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Picture 입니다."));
        PictureComment pictureComment = request.toEntity(getUser, getPicture);

        pictureComment.setCommentOrder(pictureCommentRepository.getLastPictureCommentOrder(getPicture.getId()) + 1);

        if (pictureComment.getParentCommentOrder() == null) {
            pictureComment.setParentCommentOrder(pictureComment.getCommentOrder());
        } else {
            long parentCommentOrder = pictureComment.getParentCommentOrder();
            PictureComment parentComm = pictureCommentRepository.findByCommentOrder(parentCommentOrder);
            parentComm.plusChildCommentCount();
        }
        pictureCommentRepository.save(pictureComment);

        return new PictureCommentDTO.PictureCommentDetailResponse(pictureComment);
    }

    @Override
    public List<PictureCommentDTO.PictureCommentDetailResponse> getPictureCommentByPictureId(Long pictureId) {

        List<PictureComment> pictureCommentList = pictureCommentRepository.findByPictureId(pictureId);

        return pictureCommentList.stream().map(PictureCommentDTO.PictureCommentDetailResponse::new).collect(Collectors.toList());

    }

    @Override
    public void deletePictureComment(Long pictureCommentId) {

        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 picture 댓글 입니다."));
        if (!pictureComment.getCommentOrder().equals(pictureComment.getParentCommentOrder())) {
            PictureComment parentpictureComment = pictureCommentRepository.findByCommentOrder(pictureComment.getParentCommentOrder());
            parentpictureComment.minusChildCommentCount();
            if (parentpictureComment.getChildCommentCount() == 0 && parentpictureComment.getIsDeleted())
                pictureCommentRepository.delete(parentpictureComment);

        }
        pictureCommentRepository.delete(pictureComment);

    }

    @Override
    public void deletePictureCommentWithStatus(Long pictureCommentId) {

        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElseThrow(
                () -> new EntityNotFoundException("해당 picture댓글이 존재하지 않습니다."));

        pictureComment.setDeleted();

    }

    @Override
    public PictureCommentDTO.PictureCommentDetailResponse updatePictureComment(Long pictureCommentId, PictureCommentDTO.UpdatePictureCommentRequest request) {
        PictureComment pictureComment = pictureCommentRepository.findById(pictureCommentId).orElseThrow(
                () -> new EntityNotFoundException("해당 picture 댓글이 존재하지 않습니다."));
        pictureComment.setText(request.getText());
        pictureCommentRepository.save(pictureComment);

        return new PictureCommentDTO.PictureCommentDetailResponse(pictureComment);
    }
}
