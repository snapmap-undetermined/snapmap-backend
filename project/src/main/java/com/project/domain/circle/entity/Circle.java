package com.project.domain.circle.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pin.entity.Pin;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "circle")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Circle extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToMany(mappedBy = "circle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Pin> pins = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Users master;

    @Column(name = "key")
    private String key;

    public void addPin(Pin pin) {
        this.getPins().add(pin);
        pin.setCircle(this);
    }

    public void removePin(Pin pin) {
        this.getPins().remove(pin);
        pin.setCircle(null);
    }

    public void setName(String circleName) {
        this.name = circleName;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
