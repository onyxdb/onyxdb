package com.onyxdb.mdb.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.onyxdb.mdb.common.MemoriousTransactionTemplate;
import com.onyxdb.mdb.exceptions.BadRequestException;
import com.onyxdb.mdb.models.Cluster;
import com.onyxdb.mdb.models.Project;
import com.onyxdb.mdb.models.ProjectToCreate;
import com.onyxdb.mdb.repositories.ClusterRepository;
import com.onyxdb.mdb.repositories.ProjectRepository;

/**
 * @author foxleren
 */
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ClusterRepository clusterRepository;
    private final MemoriousTransactionTemplate memoriousTransactionTemplate;

    public ProjectService(
            ProjectRepository projectRepository,
            ClusterRepository clusterRepository,
            MemoriousTransactionTemplate memoriousTransactionTemplate
    ) {
        this.projectRepository = projectRepository;
        this.clusterRepository = clusterRepository;
        this.memoriousTransactionTemplate = memoriousTransactionTemplate;
    }

    public UUID create(ProjectToCreate projectToCreate) {
//        var project = Project.fromProjectToCreate(projectToCreate);
//        projectRepository.create(project);
//        return project.id();
        return null;
    }

    public void delete(UUID id) {
        List<Cluster> clusters = clusterRepository.getByProjectId(id);
        memoriousTransactionTemplate.executeWithIsolation(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                List<Cluster> linkedClusters = clusterRepository.getByProjectId(id);
                if (!linkedClusters.isEmpty()) {
                    throw new BadRequestException(String.format(
                            "Project can't be deleted because it contains clusters; projectId=%s, clusters=%s",
                            id,
                            linkedClusters
                                    .stream()
                                    .map(Cluster::id)
                                    .map(UUID::toString)
                                    .collect(Collectors.joining(","))
                    ));
                }
                projectRepository.delete(id);
            }
        }, TransactionDefinition.ISOLATION_SERIALIZABLE);
    }
}
