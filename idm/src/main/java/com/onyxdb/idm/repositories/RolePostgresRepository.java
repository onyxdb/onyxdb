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
                .fetchOptional(record -> new Role(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public List<Role> findAll() {
        return dslContext.selectFrom(roleTable)
                .fetch(record -> new Role(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public void create(Role role) {
        dslContext.insertInto(roleTable)
                .set(roleTable.ID, role.id())
                .set(roleTable.NAME, role.name())
                .set(roleTable.DESCRIPTION, role.description())
                .set(roleTable.CREATED_AT, role.createdAt())
                .set(roleTable.UPDATED_AT, role.updatedAt())
                .execute();
    }

    @Override
    public void update(Role role) {
        dslContext.update(roleTable)
                .set(roleTable.NAME, role.name())
                .set(roleTable.DESCRIPTION, role.description())
                .set(roleTable.UPDATED_AT, role.updatedAt())
                .where(roleTable.ID.eq(role.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(roleTable)
                .where(roleTable.ID.eq(id))
                .execute();
    }
}