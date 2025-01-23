package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.tables.OrganizationTable;
import com.onyxdb.idm.generated.jooq.tables.ProjectTable;
import com.onyxdb.idm.generated.jooq.tables.ServiceTable;
import com.onyxdb.idm.models.OrganizationDTO;
import com.onyxdb.idm.models.ProjectDTO;
import com.onyxdb.idm.models.ServiceDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProjectPostgresRepository implements ProjectRepository {
    private final DSLContext dslContext;
    private final ProjectTable projectTable = ProjectTable.PROJECT_TABLE;
    private final OrganizationTable organizationTable = OrganizationTable.ORGANIZATION_TABLE;
    private final ServiceTable serviceTable = ServiceTable.SERVICE_TABLE;
    private final OrganizationPostgresRepository organizationRepository;
    private final ServicePostgresRepository serviceRepository;

    @Override
    public Optional<ProjectDTO> findById(UUID id) {
        return dslContext.selectFrom(projectTable)
                .where(projectTable.ID.eq(id))
                .fetchOptional()
                .map(record -> {
                    OrganizationDTO organization = dslContext.selectFrom(organizationTable)
                            .where(organizationTable.ID.eq(record.getOrganizationId()))
                            .fetchOptional()
                            .map(orgRecord -> organizationRepository.findById(orgRecord.getId()).orElse(null))
                            .orElse(null);

                    List<ServiceDTO> services = dslContext.selectFrom(serviceTable)
                            .where(serviceTable.PROJECT_ID.eq(id))
                            .fetch()
                            .map(serviceRecord -> serviceRepository.findById(serviceRecord.getId()).orElse(null))
                            .stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());

                    return ProjectDTO.builder()
                            .id(record.getId())
                            .name(record.getName())
                            .organizationId(record.getOrganizationId())
                            .organization(organization)
                            .services(services)
                            .build();
                });
    }

    @Override
    public List<ProjectDTO> findByOrganizationId(UUID organizationId) {
        return dslContext.selectFrom(projectTable)
                .where(projectTable.ORGANIZATION_ID.eq(organizationId))
                .fetch()
                .map(record -> ProjectDTO.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .organizationId(record.getOrganizationId())
                        .build());
    }

    @Override
    public void create(ProjectDTO project) {
        dslContext.insertInto(projectTable)
                .set(projectTable.ID, project.getId())
                .set(projectTable.NAME, project.getName())
                .set(projectTable.ORGANIZATION_ID, project.getOrganizationId())
                .execute();
    }

    @Override
    public void update(ProjectDTO project) {
        dslContext.update(projectTable)
                .set(projectTable.NAME, project.getName())
                .set(projectTable.ORGANIZATION_ID, project.getOrganizationId())
                .where(projectTable.ID.eq(project.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(projectTable)
                .where(projectTable.ID.eq(id))
                .execute();
    }
}