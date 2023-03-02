package com.project.domain.comment.api;

import com.project.domain.comment.dto.PinCommentDTO;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface PinCommentService {

    PinCommentDTO.PinCommentDetailResponse createPinComment(Users user, PinCommentDTO.CreatePinCommentRequest request);

    List<PinCommentDTO.PinCommentDetailResponse> getPinCommentByPinId(Long pinId);

    void deletePinComment(Long pinCommentId);

    void deletePinCommentWithStatus(Long pinCommentId);

    PinCommentDTO.PinCommentDetailResponse updatePinComment(Long pinCommentId, PinCommentDTO.UpdatePinCommentRequest request);
}
