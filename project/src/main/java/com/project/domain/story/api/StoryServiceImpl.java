package com.project.domain.story.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import com.project.common.handler.S3Uploader;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.circlestory.entity.CircleStory;
import com.project.domain.circlestory.repository.CircleStoryRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
import com.project.domain.story.dto.StoryDTO;
import com.project.domain.story.entity.Story;
import com.project.domain.story.repository.StoryRepository;
import com.project.domain.storypicture.entity.StoryPicture;
import com.project.domain.storypicture.repository.StoryPictureRepository;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final PictureRepository pictureRepository;
    private final CircleRepository circleRepository;
    private final CircleStoryRepository circleStoryRepository;
    private final StoryPictureRepository storyPictureRepository;
    private final S3Uploader s3Uploader;


    @Override
    public StoryDTO.StoryDetailResponse createStory(Users user, Long circleId, StoryDTO.StoryCreateRequest request, List<MultipartFile> pictures) {
        try {
            Story story = request.toEntity();
            Circle circle = circleRepository.findById(circleId).orElseThrow(
                    () -> new EntityNotFoundException("존재하지 않는 써클입니다."));

            story.setUser(user);
            storyRepository.save(story);

            for (MultipartFile picture : pictures) {
                // S3에 사진 업로드
                Map<String, String> result = s3Uploader.upload(picture, "static");
                String pictureName = result.get("originalName");
                String uploadUrl = result.get("uploadUrl");

                // Picture 생성
                Picture savedPicture = pictureRepository.save(Picture.builder().originalName(pictureName).url(uploadUrl).build());
                story.addPicture(savedPicture);

                // Story - Picture 맵핑 정보 생성
                storyPictureRepository.save(StoryPicture.builder().story(story).picture(savedPicture).build());

                // Story - Circle 맵핑 정보 생성
                circleStoryRepository.save(CircleStory.builder().circle(circle).story(story).build());
            }

            return new StoryDTO.StoryDetailResponse(story);
        } catch (ParseException e) {
            throw new InvalidValueException("스토리를 생성하는 데 실패했습니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @Override
    public StoryDTO.StoryDetailResponse getStoryDetail(Users user, Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 스토리입니다."));
        return new StoryDTO.StoryDetailResponse(story);
    }

    @Override
    public List<StoryDTO.StoryDetailResponse> getAllStoryByCircle(Long circleId) {
        return storyRepository.findAllByCircleId(circleId).stream().map(StoryDTO.StoryDetailResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<StoryDTO.StoryDetailResponse> getAllStoryByMe(Users user) {
        return storyRepository.findByUserId(user.getId()).stream().map(StoryDTO.StoryDetailResponse::new).collect(Collectors.toList());
    }

    @Override
    public StoryDTO.StoryDetailResponse updateStory(Users user, Long storyId, StoryDTO.StoryUpdateRequest request) throws ParseException {
        Story story = storyRepository.findById(storyId).orElseThrow(() -> {
            log.error("Delete story failed. storyId = {}", storyId);
            throw new EntityNotFoundException("존재하지 않는 스토리입니다.");
        });

        story.setLocation(StoryDTO.toPoint(request.getLocation()));

        // 사진 수정
        List<MultipartFile> newPictureList = request.getPictures();

        story.getPictureList().clear();

        newPictureList.forEach((picture) -> {
            Map<String, String> result = s3Uploader.upload(picture, "static");
            String pictureName = result.get("originalName");
            String uploadUrl = result.get("uploadUrl");

            story.addPicture(Picture.builder().originalName(pictureName).url(uploadUrl).build());
        });

        return new StoryDTO.StoryDetailResponse(story);
    }

    @Override
    public void deleteStory(Users user, Long storyId) {
        storyRepository.delete(
            storyRepository.findById(storyId).orElseThrow(() -> {
                log.error("Delete story failed. storyId = {}", storyId);
                throw new EntityNotFoundException("존재하지 않는 스토리입니다.");
            })
        );
    }
}
