package com.onyxdb.idm.repositories;

import com.onyxdb.idm.codegen.types.RoleType;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.RoleDTO;
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
    private final RoleTable roleTable = RoleTable.ROLE_TABLE;

    @Override
    public Optional<RoleDTO> findById(UUID id) {
        return dslContext.selectFrom(roleTable)
                .where(roleTable.ID.eq(id))
                .fetchOptional()
                .map(record -> RoleDTO.builder()
                        .id(record.getId())
                        .name(RoleType.valueOf(record.getName()))
                        .permissions(List.of(record.getPermissions()))
                        .build());
    }

    @Override
    public List<RoleDTO> findAll() {
        return dslContext.selectFrom(roleTable)
                .fetch()
                .map(record -> RoleDTO.builder()
                        .id(record.getId())
                        .name(RoleType.valueOf(record.getName()))
                        .permissions(List.of(record.getPermissions()))
                        .build());
    }

    @Override
    public void create(RoleDTO role) {
        dslContext.insertInto(roleTable)
                .set(roleTable.ID, role.getId())
                .set(roleTable.NAME, role.getName().name())
                .set(roleTable.PERMISSIONS, role.getPermissions().toArray(new String[0]))
                .execute();
    }

    @Override
    public void update(RoleDTO role) {
        dslContext.update(roleTable)
                .set(roleTable.NAME, role.getName().name())
                .set(roleTable.PERMISSIONS, role.getPermissions().toArray(new String[0]))
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