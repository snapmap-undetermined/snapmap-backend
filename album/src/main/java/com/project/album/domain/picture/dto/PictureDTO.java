package com.project.album.domain.picture.dto;

import com.project.album.domain.picture.entity.Picture;
import lombok.Data;

public class PictureDTO {

    @Data
    public static class PictureResponse {
        private Long id;
        private String uri;

        public PictureResponse(Picture picture) {
            this.id = picture.getId();
            this.uri = picture.getUrl();
        }
    }

}


