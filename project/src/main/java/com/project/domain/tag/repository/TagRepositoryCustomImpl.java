package com.project.domain.tag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepositoryCustomImpl implements TagRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


}
