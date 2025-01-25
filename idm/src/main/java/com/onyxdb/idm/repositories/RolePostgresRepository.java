package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.Role;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RolePostgresRepository implements RoleRepository {

    private final DSLContext dslContext;
    private final RoleTable roleTable = Tables.ROLE_TABLE;

    @Override
    public Optional<Role> findById(UUID id) {
        return dslContext.selectFrom(roleTable)
                .where(roleTable.ID.eq(id))
                .fetchOptional(record -> Role.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public List<Role> findAll() {
        return dslContext.selectFrom(roleTable)
                .fetch(record -> Role.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public void create(Role role) {
        dslContext.insertInto(roleTable)
                .set(roleTable.ID, role.getId())
                .set(roleTable.NAME, role.getName())
                .set(roleTable.DESCRIPTION, role.getDescription())
                .set(roleTable.CREATED_AT, role.getCreatedAt())
                .set(roleTable.UPDATED_AT, role.getUpdatedAt())
                .execute();
    }

    @Override
    public void update(Role role) {
        dslContext.update(roleTable)
                .set(roleTable.NAME, role.getName())
                .set(roleTable.DESCRIPTION, role.getDescription())
                .set(roleTable.UPDATED_AT, role.getUpdatedAt())
                .where(roleTable.ID.eq(role.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(roleTable)
                .where(roleTable.ID.eq(id))
                .execute();
    }
}