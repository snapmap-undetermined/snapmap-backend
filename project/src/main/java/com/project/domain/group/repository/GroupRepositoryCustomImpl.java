package com.project.domain.group.repository;

import com.project.domain.group.entity.GroupData;
import com.project.domain.users.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.group.entity.QGroupData.groupData;
import static com.project.domain.usergroup.entity.QUserGroup.userGroup;
import static com.project.domain.users.entity.QUsers.users;


@RequiredArgsConstructor
public class GroupRepositoryCustomImpl implements GroupRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<GroupData> findAllGroupByUserId(Long userId) {
        return query
                .selectFrom(groupData)
                .innerJoin(groupData.userGroupList, userGroup)
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
    public GroupData findGroupByKey(String key) {
        return query
                .selectFrom(groupData)
                .where(groupData.groupKey.eq(key))
                .fetchOne();
    }
}
