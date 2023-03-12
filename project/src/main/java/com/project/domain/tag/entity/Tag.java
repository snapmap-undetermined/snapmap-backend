package com.project.domain.tag.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pintag.entity.PinTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tag")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Tag extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PinTag> pinTags = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }
}
