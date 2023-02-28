package com.project.domain.pinhashtag.entity;

import com.project.domain.hashtag.entity.Hashtag;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pin_hashtag")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PinHashtag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "pin_hashtag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

}
