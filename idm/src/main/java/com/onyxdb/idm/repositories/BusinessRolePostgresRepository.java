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
                .fetchOptional(record -> new BusinessRole(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public List<BusinessRole> findAll() {
        return dslContext.selectFrom(businessRoleTable)
                .fetch(record -> new BusinessRole(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }


    @Override
    public List<BusinessRole> findByParentId(UUID parentId) {
        return dslContext.selectFrom(businessRoleTable)
                .where(businessRoleTable.PARENT_ID.eq(parentId))
                .fetch(record -> new BusinessRole(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public void create(BusinessRole businessRole) {
        dslContext.insertInto(businessRoleTable)
                .set(businessRoleTable.ID, businessRole.id())
                .set(businessRoleTable.NAME, businessRole.name())
                .set(businessRoleTable.DESCRIPTION, businessRole.description())
                .set(businessRoleTable.PARENT_ID, businessRole.parentId())
                .set(businessRoleTable.CREATED_AT, businessRole.createdAt())
                .set(businessRoleTable.UPDATED_AT, businessRole.updatedAt())
                .execute();
    }

    @Override
    public void update(BusinessRole businessRole) {
        dslContext.update(businessRoleTable)
                .set(businessRoleTable.NAME, businessRole.name())
                .set(businessRoleTable.DESCRIPTION, businessRole.description())
                .set(businessRoleTable.PARENT_ID, businessRole.parentId())
                .set(businessRoleTable.UPDATED_AT, businessRole.updatedAt())
                .where(businessRoleTable.ID.eq(businessRole.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(businessRoleTable)
                .where(businessRoleTable.ID.eq(id))
                .execute();
    }
}
