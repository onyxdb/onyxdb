package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.BusinessRoleTable;
import com.onyxdb.idm.models.BusinessRole;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BusinessRolePostgresRepository implements BusinessRoleRepository {

    private final DSLContext dslContext;
    private final BusinessRoleTable businessRoleTable = Tables.BUSINESS_ROLE_TABLE;

    @Override
    public Optional<BusinessRole> findById(UUID id) {
        return dslContext.selectFrom(businessRoleTable)
                .where(businessRoleTable.ID.eq(id))
                .fetchOptional(record -> BusinessRole.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .parentId(record.getParentId())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public List<BusinessRole> findAll() {
        return dslContext.selectFrom(businessRoleTable)
                .fetch(record -> BusinessRole.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .parentId(record.getParentId())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public void create(BusinessRole businessRole) {
        dslContext.insertInto(businessRoleTable)
                .set(businessRoleTable.ID, businessRole.getId())
                .set(businessRoleTable.NAME, businessRole.getName())
                .set(businessRoleTable.DESCRIPTION, businessRole.getDescription())
                .set(businessRoleTable.PARENT_ID, businessRole.getParentId())
                .set(businessRoleTable.CREATED_AT, businessRole.getCreatedAt())
                .set(businessRoleTable.UPDATED_AT, businessRole.getUpdatedAt())
                .execute();
    }

    @Override
    public void update(BusinessRole businessRole) {
        dslContext.update(businessRoleTable)
                .set(businessRoleTable.NAME, businessRole.getName())
                .set(businessRoleTable.DESCRIPTION, businessRole.getDescription())
                .set(businessRoleTable.PARENT_ID, businessRole.getParentId())
                .set(businessRoleTable.UPDATED_AT, businessRole.getUpdatedAt())
                .where(businessRoleTable.ID.eq(businessRole.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(businessRoleTable)
                .where(businessRoleTable.ID.eq(id))
                .execute();
    }

    @Override
    public List<BusinessRole> findByParentId(UUID parentId) {
        return dslContext.selectFrom(businessRoleTable)
                .where(businessRoleTable.PARENT_ID.eq(parentId))
                .fetch(record -> BusinessRole.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .parentId(record.getParentId())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }
}
