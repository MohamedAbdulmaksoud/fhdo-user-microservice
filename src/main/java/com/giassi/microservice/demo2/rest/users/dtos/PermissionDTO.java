package com.giassi.microservice.demo2.rest.users.dtos;

import com.giassi.microservice.demo2.rest.users.entities.Permission;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class PermissionDTO implements java.io.Serializable {

    private UUID id;
    private String permission;
    private boolean enabled;
    private String note;

    public PermissionDTO(Permission permission) {
        this.id = permission.getId();
        this.permission = permission.getPermission();
        this.enabled = permission.getEnabled().equals(Boolean.TRUE);
        this.note = permission.getNote();
    }

}
