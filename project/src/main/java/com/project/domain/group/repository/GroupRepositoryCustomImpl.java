package com.project.domain.group.repository;

import com.project.domain.group.entity.Groups;
import com.project.domain.users.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.group.entity.QGroups.groups;
import static com.project.domain.usergroup.entity.QUserGroup.userGroup;
import static com.project.domain.users.entity.QUsers.users;


@RequiredArgsConstructor
public class GroupRepositoryCustomImpl implements GroupRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Groups> findAllGroupByUserId(Long userId) {
        return query
                .selectFrom(groups)
                .innerJoin(groups.userGroupList, userGroup)
                .fetchJoin()
                .where(userGroup.user.id.eq(userId))
                .where(userGroup.activated.eq(true))
                .distinct()
                .fetch();

    }

    @Override
    public List<Users> findAllUserByGroupId(Long groupId) {

        return query
                .selectFrom(users)
                .innerJoin(users.userGroupList, userGroup)
                .fetchJoin()
                .where(userGroup.group.id.eq(groupId))
                .where(userGroup.activated.eq(true))
                .distinct()
                .fetch();
    }

    @Override
    public Groups findGroupByKey(String key) {
        return query
                .selectFrom(groups)
                .where(groups.groupKey.eq(key))
                .fetchOne();
    }
}
