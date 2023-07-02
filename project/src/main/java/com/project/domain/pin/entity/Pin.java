package com.project.domain.pin.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pocket.entity.Pocket;
import com.project.domain.comment.entity.PinComment;
import com.project.domain.location.entity.Location;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pintag.entity.PinTag;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pin")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Pin extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pocket pocket;

    // 하나의 장소에 대응되는 여러 개의 핀이 존재 가능하다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PinTag> pinTags = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Picture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<PinComment> commentList = new ArrayList<>();

    public void setPocket(Pocket pocket) {
        this.pocket = pocket;
    }

    public void addPicture(Picture picture) {
        if (!getPictures().contains(picture)) {
            getPictures().add(picture);
        }
        picture.setPin(this);
    }

    public void addPinTag(PinTag pinTag) {
        if (!getPinTags().contains(pinTag)) {
            getPinTags().add(pinTag);
        }
        pinTag.setPin(this);
    }

    public void removePicture(Picture picture) {
        getPictures().remove(picture);
        picture.setPin(null);
    }

    public void removePinTag(PinTag pinTag) {
        getPinTags().remove(pinTag);
        pinTag.setPin(this);
    }


    public void setUser(Users user) {
        this.user = user;
    }

    public void setLocation(Location location) {
        if (this.location != null) {
            this.location.getPins().remove(this);
        }
        this.location = location;
        location.getPins().add(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
