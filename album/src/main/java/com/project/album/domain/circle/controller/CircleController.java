package com.project.album.domain.circle.controller;

import com.project.album.domain.circle.api.CircleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/circle")
public class CircleController {

    private final CircleService circleService;

    //그룹을 생성한다.

    //유저별로 그룹을 조회한다.

    //그룹을 삭제한다.
}
