package com.project.domain.pinpicture.repository;

import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.entity.QFriend;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pin.entity.QPin;
import com.project.domain.pinpicture.entity.QPinPicture;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.pinpicture.entity.QPinPicture.pinPicture;

@RequiredArgsConstructor
public class PinPictureRepositoryCustomImpl implements PinPictureRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Picture> findAllPicturesByPinId(Long pinId) {
        return query.select(pinPicture.picture)
                .from(pinPicture)
                .where(pinPicture.pin.id.eq(pinId))
                .fetch();
    }
}
