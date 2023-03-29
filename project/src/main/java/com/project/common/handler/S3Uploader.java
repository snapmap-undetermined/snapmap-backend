package com.project.common.handler;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.ErrorCode;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.snapmap-cloud-front-domain}")
    private String cloudFrontDomain;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final PictureRepository pictureRepository;

    public Map<String, String> upload(MultipartFile multipartFile, String dirName) {
        File uploadFile = null;
        try {
            uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new BusinessLogicException("MultipartFile -> File로 전환이 실패했습니다.", ErrorCode.HANDLE_PICTURE_UPLOAD));
        } catch (IOException e) {
            throw new BusinessLogicException("MultipartFile -> File로 전환이 실패했습니다.", ErrorCode.HANDLE_PICTURE_UPLOAD);
        }
        return upload(uploadFile, dirName);
    }

    private Map<String, String> upload(File uploadFile, String dirName) {
        Map<String, String> result = new HashMap<>();
        String fileName = uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        result.put("originalName", uploadFile.getName());
        result.put("uploadUrl", "https://" + cloudFrontDomain + "/" + uploadImageUrl);
        return result;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("targetFile : {} is deleted.", targetFile.getName());
        } else {
            log.error("targetFile : {} is not deleted.", targetFile.getName());
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        log.info("MultipartFile -> File converted : {} -> {}", file.getOriginalFilename(), convertFile.getName());

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public List<Picture> uploadAndSavePictures(List<MultipartFile> pictureList) {
        return pictureList.stream().map((p) -> {
            // S3에 사진 업로드
            Map<String, String> result = upload(p, "static");
            String pictureName = result.get("originalName");
            String uploadUrl = result.get("uploadUrl");
            // Picture 생성
            return pictureRepository.save(Picture.builder().originalName(pictureName).url(uploadUrl).build());
        }).toList();
    }

    public String uploadAndSaveImage(MultipartFile picture) {
        // S3에 사진 업로드
        Map<String, String> result = upload(picture, "static");
        return result.get("uploadUrl");
    }
}
