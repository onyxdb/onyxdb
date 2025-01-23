package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.tables.OrganizationTable;
import com.onyxdb.idm.generated.jooq.tables.ProjectTable;
import com.onyxdb.idm.models.OrganizationDTO;
import com.onyxdb.idm.models.ProjectDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrganizationPostgresRepository implements OrganizationRepository {
    private final DSLContext dslContext;
    private final OrganizationTable organizationTable = OrganizationTable.ORGANIZATION_TABLE;
    private final ProjectTable projectTable = ProjectTable.PROJECT_TABLE;
    private final ProjectPostgresRepository projectRepository;

    @Override
    public Optional<OrganizationDTO> findById(UUID id) {
        return dslContext.selectFrom(organizationTable)
                .where(organizationTable.ID.eq(id))
                .fetchOptional()
                .map(record -> {
                    List<ProjectDTO> projects = dslContext.selectFrom(projectTable)
                            .where(projectTable.ORGANIZATION_ID.eq(id))
                            .fetch()
                            .map(projectRecord -> projectRepository.findById(projectRecord.getId()).orElse(null))
                            .stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());

                    return OrganizationDTO.builder()
                            .id(record.getId())
                            .name(record.getName())
                            .projects(projects)
                            .build();
                });
    }

    @Override
    public List<OrganizationDTO> findAll() {
        return dslContext.selectFrom(organizationTable)
                .fetch()
                .map(record -> OrganizationDTO.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .build());
    }

    @Override
    public void create(OrganizationDTO organization) {
        dslContext.insertInto(organizationTable)
                .set(organizationTable.ID, organization.getId())
                .set(organizationTable.NAME, organization.getName())
                .execute();
    }

    @Override
    public void update(OrganizationDTO organization) {
        dslContext.update(organizationTable)
                .set(organizationTable.NAME, organization.getName())
                .where(organizationTable.ID.eq(organization.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(organizationTable)
                .where(organizationTable.ID.eq(id))
                .execute();
    }
}