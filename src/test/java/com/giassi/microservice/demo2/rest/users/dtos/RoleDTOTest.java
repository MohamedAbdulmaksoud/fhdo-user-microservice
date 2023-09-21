package com.giassi.microservice.demo2.rest.users.dtos;

import com.giassi.microservice.demo2.rest.users.entities.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RoleDTOTest {

    @Test
    public void testRoleDTOConstructor1() {
        Role role = new Role(UUID.fromString("08cb3618-63ab-4c9f-80a3-928bc2bfe632"), "USER");

        RoleDTO roleDTO = new RoleDTO(role);

        assertEquals(role.getId(), roleDTO.getId());
        assertEquals(role.getRole(), roleDTO.getRole());
    }

    @Test
    public void testRoleDTOConstructor2() {
        RoleDTO roleDTO = new RoleDTO(UUID.fromString("08cb3618-63ab-4c9f-80a3-928bc2bfe632"), "USER");

        assertEquals("08cb3618-63ab-4c9f-80a3-928bc2bfe632", roleDTO.getId().toString());
        assertEquals("USER", roleDTO.getRole());
    }

}
