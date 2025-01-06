package com.onyxdb.mongodbOperator.utils;

import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;

/**
 * @author foxleren
 */
public final class PodsUtil {
    public static boolean isPodRunning(Pod pod) {
        return pod.getStatus().getPhase().equalsIgnoreCase(PodPhase.RUNNING.toString());
    }

    public static boolean isPodContainerReady(Pod pod, String containerName) {
        return pod.getStatus()
                .getContainerStatuses()
                .stream()
                .filter(containerStatus -> containerStatus.getName().equalsIgnoreCase(containerName))
                .findFirst()
                .map(ContainerStatus::getReady)
                .orElse(false);
    }
}
