package com.project.domain.story.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.story.api.StoryService;
import com.project.domain.story.dto.StoryDTO;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/story")
public class StoryController {

    private final StoryService storyService;


    @PostMapping("/{circleId}")
    @Permission
    public ResponseEntity<StoryDTO.StoryDetailResponse> createStory(@AuthUser Users user, @PathVariable Long circleId,
                                                                    @RequestPart StoryDTO.StoryCreateRequest request,
                                                                    @RequestPart List<MultipartFile> pictures) {

        System.out.println("request = " + request);
        System.out.println("pictures = " + pictures);
        StoryDTO.StoryDetailResponse story = storyService.createStory(user, circleId, request, pictures);
        return new ResponseEntity<>(story, HttpStatus.OK);
    }

    @GetMapping("/{circleId}")
    @Permission
    public ResponseEntity<List<StoryDTO.StoryDetailResponse>> getAllStoryByCircle(@AuthUser Users user, @PathVariable Long circleId) {
        List<StoryDTO.StoryDetailResponse> storyList = storyService.getAllStoryByCircle(circleId);
        return new ResponseEntity<>(storyList, HttpStatus.OK);
    }

    @GetMapping("/{storyId}")
    @Permission
    public ResponseEntity<StoryDTO.StoryDetailResponse> getStory(@AuthUser Users user, @PathVariable Long storyId) {
        StoryDTO.StoryDetailResponse story = storyService.getStoryDetail(user, storyId);
        return new ResponseEntity<>(story, HttpStatus.OK);
    }

    @GetMapping("/me")
    @Permission
    public ResponseEntity<List<StoryDTO.StoryDetailResponse>> getAllStoryByMe(@AuthUser Users user) {
        List<StoryDTO.StoryDetailResponse> storyList = storyService.getAllStoryByMe(user);
        return new ResponseEntity<>(storyList, HttpStatus.OK);
    }

    @PatchMapping("/{storyId}")
    @Permission
    public ResponseEntity<StoryDTO.StoryDetailResponse> updateStory(@AuthUser Users user, @PathVariable Long storyId, @RequestBody StoryDTO.StoryUpdateRequest request) throws Exception{
        StoryDTO.StoryDetailResponse story = storyService.updateStory(user, storyId, request);
        return new ResponseEntity<>(story, HttpStatus.OK);
    }

    @DeleteMapping("/{storyId}")
    @Permission
    public ResponseEntity<Long> deleteStory(@AuthUser Users user, @PathVariable Long storyId) {
        storyService.deleteStory(user, storyId);
        return new ResponseEntity<>(storyId, HttpStatus.OK);
    }
}
