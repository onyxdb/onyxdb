package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.tables.GroupTable;
import com.onyxdb.idm.models.GroupDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GroupPostgresRepository implements GroupRepository {
    private final DSLContext dslContext;
    private final GroupTable groupTable = GroupTable.GROUP_TABLE;

    @Override
    public Optional<GroupDTO> findById(UUID id) {
        return dslContext.selectFrom(groupTable)
                .where(groupTable.ID.eq(id))
                .fetchOptional()
                .map(record -> GroupDTO.builder()
                        .id(record.getId())
                        .name(record.getName())
                        // Добавьте остальные поля при необходимости
                        .build());
    }

    @Override
    public List<GroupDTO> findAll() {
        return dslContext.selectFrom(groupTable)
                .fetch()
                .map(record -> GroupDTO.builder()
                        .id(record.getId())
                        .name(record.getName())
                        // Добавьте остальные поля при необходимости
                        .build());
    }

    @Override
    public void create(GroupDTO group) {
        dslContext.insertInto(groupTable)
                .set(groupTable.ID, group.getId())
                .set(groupTable.NAME, group.getName())
                // Добавьте остальные поля при необходимости
                .execute();
    }

    @Override
    public void update(GroupDTO group) {
        dslContext.update(groupTable)
                .set(groupTable.NAME, group.getName())
                // Добавьте остальные поля при необходимости
                .where(groupTable.ID.eq(group.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(groupTable)
                .where(groupTable.ID.eq(id))
                .execute();
    }
}

