package com.onyxdb.idm.models;

import lombok.Getter;

@Getter
public enum RoleRequestStatus {
    WAITING("WAITING"),
    APPROVED("APPROVED"),
    DECLINED("DECLINED");
    private final String status;

    RoleRequestStatus(String status) {
        this.status = status;
    }
}
