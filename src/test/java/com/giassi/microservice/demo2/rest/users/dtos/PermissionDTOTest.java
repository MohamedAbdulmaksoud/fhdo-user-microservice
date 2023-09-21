package com.giassi.microservice.demo2.rest.users.dtos;

import com.giassi.microservice.demo2.rest.users.entities.Permission;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PermissionDTOTest {

    @Test
    public void testPermissionDTOConstructor1() {
        PermissionDTO permissionDTO = new PermissionDTO();

        assertEquals(null, permissionDTO.getId());
        assertEquals(null, permissionDTO.getPermission());
    }

    @Test
    public void testPermissionDTOConstructor2() {
        Permission permission = new Permission(UUID.randomUUID(), "Browse website");

        PermissionDTO permissionDTO = new PermissionDTO(permission);

        assertEquals(permission.getId(), permissionDTO.getId());
        assertEquals(permission.getPermission(), permissionDTO.getPermission());
        assertTrue(permissionDTO.isEnabled());
    }

}
