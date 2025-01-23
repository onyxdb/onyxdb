package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.tables.ProjectTable;
import com.onyxdb.idm.generated.jooq.tables.ServiceTable;
import com.onyxdb.idm.models.ProjectDTO;
import com.onyxdb.idm.models.ServiceDTO;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ServicePostgresRepository implements ServiceRepository {
    private final DSLContext dslContext;
    private final ServiceTable serviceTable = ServiceTable.SERVICE_TABLE;
    private final ProjectTable projectTable = ProjectTable.PROJECT_TABLE;
    private final ProjectPostgresRepository projectRepository;

    @Override
    public Optional<ServiceDTO> findById(UUID id) {
        return dslContext.selectFrom(serviceTable)
                .where(serviceTable.ID.eq(id))
                .fetchOptional()
                .map(record -> {
                    ProjectDTO project = dslContext.selectFrom(projectTable)
                            .where(projectTable.ID.eq(record.getProjectId()))
                            .fetchOptional()
                            .map(projectRecord -> projectRepository.findById(projectRecord.getId()).orElse(null))
                            .orElse(null);

                    return ServiceDTO.builder()
                            .id(record.getId())
                            .name(record.getName())
                            .type(record.getType())
                            .projectId(record.getProjectId())
                            .project(project)
                            .build();
                });
    }

    @Override
    public List<ServiceDTO> findByProjectId(UUID projectId) {
        return dslContext.selectFrom(serviceTable)
                .fetch()
                .map(record -> ServiceDTO.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .type(record.getType())
                        .projectId(record.getProjectId())
                        .build());
    }

    @Override
    public void create(ServiceDTO service) {
        dslContext.insertInto(serviceTable)
                .set(serviceTable.ID, service.getId())
                .set(serviceTable.NAME, service.getName())
                .set(serviceTable.TYPE, service.getType())
                .set(serviceTable.PROJECT_ID, service.getProjectId())
                .execute();
    }

    @Override
    public void update(ServiceDTO service) {
        dslContext.update(serviceTable)
                .set(serviceTable.NAME, service.getName())
                .set(serviceTable.TYPE, service.getType())
                .set(serviceTable.PROJECT_ID, service.getProjectId())
                .where(serviceTable.ID.eq(service.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(serviceTable)
                .where(serviceTable.ID.eq(id))
                .execute();
    }
}