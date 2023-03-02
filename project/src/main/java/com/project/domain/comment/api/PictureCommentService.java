package com.project.domain.comment.api;

import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface PictureCommentService {

    PictureCommentDTO.PictureCommentDetailResponse createPictureComment(Users user, PictureCommentDTO.CreatePictureCommentRequest request);

    List<PictureCommentDTO.PictureCommentDetailResponse> getPictureCommentByPictureId(Long pictureId);

    void deletePictureComment(Long pictureCommentId);

    void deletePictureCommentWithStatus(Long pictureCommentId);

    PictureCommentDTO.PictureCommentDetailResponse updatePictureComment(Long pictureCommentId, PictureCommentDTO.UpdatePictureCommentRequest request);

}
