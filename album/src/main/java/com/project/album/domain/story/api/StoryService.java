package com.project.album.domain.story.api;

import com.project.album.domain.story.dto.StoryDTO;
import com.project.album.domain.users.entity.Users;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryService {
    StoryDTO.StoryDetailResponse createStory(Users user, Long circleId, StoryDTO.StoryCreateRequest request, List<MultipartFile> pictures);

    StoryDTO.StoryDetailResponse getStoryDetail(Users user, Long feedId);

    List<StoryDTO.StoryDetailResponse> getAllStoryByMe(Users user);

    StoryDTO.StoryDetailResponse updateStory(Users user, Long feedId, StoryDTO.StoryUpdateRequest request) throws ParseException;

    void deleteStory(Users user, Long feedId);

    List<StoryDTO.StoryDetailResponse> getAllStoryByCircle(Long circleId);
}
