package com.project.domain.pintag.entity;

import com.project.domain.tag.entity.Tag;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pin_tag")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PinTag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private Pin pin;

    @ManyToOne
    private Tag tag;

    public void setPin(Pin pin) {
        if (this.pin != null) {
            this.pin.getPinTags().remove(this);
        }
        this.pin = pin;
        pin.getPinTags().add(this);
    }

    public void setTag(Tag tag) {
        if (this.tag != null) {
            this.tag.getPinTags().remove(this);
        }
        this.tag = tag;
        tag.getPinTags().add(this);
    }
}
