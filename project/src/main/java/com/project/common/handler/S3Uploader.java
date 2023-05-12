package com.project.common.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.ErrorCode;
import com.project.common.utils.FileUtils;
import com.project.domain.picture.dto.PictureDetail;
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

    @Value("${cloud.aws.cloud-front-domain}")
    private String cloudFrontDomain;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final PictureRepository pictureRepository;

    public void upload(MultipartFile multipartFile, String path) {
        File uploadFile = convert(multipartFile, path).orElseThrow(() ->
                new BusinessLogicException("Convert multipartFile to File failed.", ErrorCode.IMAGE_PROCESSING_ERROR));

        try {
            multipartFile.transferTo(uploadFile);
            putS3(uploadFile, path);
        } catch (IOException e) {
            throw new BusinessLogicException("MultipartFile transfer failed.", ErrorCode.IMAGE_PROCESSING_ERROR);
        } catch (SdkClientException e) {
            throw new BusinessLogicException("Upload to S3 failed.", ErrorCode.IMAGE_PROCESSING_ERROR);
        } finally {
            if (uploadFile.delete()) {
                log.info("targetFile : {} is deleted.", uploadFile.getName());
            } else {
                log.error("targetFile : {} is not deleted.", uploadFile.getName());
            }
        }
    }

    private void putS3(File uploadFile, String path) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, path, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private Optional<File> convert(MultipartFile file, String fullPath) {
        File convertFile = new File(FileUtils.getLocalHomeDirectory(), fullPath);
        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            throw new BusinessLogicException("Convert MultipartFile to File Failed.", ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return Optional.empty();
    }

    public List<Picture> uploadAndSavePictures(List<MultipartFile> pictureList) {
        return pictureList.stream().map((picture) -> {
            PictureDetail pictureDetail = PictureDetail.multipartOf(picture);
            // S3에 사진 업로드
            String path = pictureDetail.getPath();
            upload(picture, path);
            // Picture 생성
            return pictureRepository.save(Picture.builder().originalName(pictureDetail.getName()).url("https://" + cloudFrontDomain + "/" + path).build());
        }).toList();
    }
}
