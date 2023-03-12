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
    @Column(name = "pin_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public void setPin(Pin pin) {
        this.pin = pin;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
