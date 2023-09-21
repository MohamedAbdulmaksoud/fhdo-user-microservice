package com.giassi.microservice.demo2.rest.users.services;

import com.giassi.microservice.demo2.rest.users.entities.Permission;
import com.giassi.microservice.demo2.rest.users.repositories.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    public void calling_getPermissionList_then_return_list_of_permissions() {
        ArrayList<Permission> permissionArrayList = new ArrayList<>();

        Permission permission1 = new Permission(UUID.fromString("5a753403-d616-4b60-bc45-205ca614b669"), "LOGIN");
        Permission permission2 = new Permission(UUID.fromString("ada87f83-23a5-48ed-9a84-e55dcd7c6e05"), "VIEW_PROFILE");

        permissionArrayList.add(permission1);
        permissionArrayList.add(permission2);

        given(permissionRepository.findAll()).willReturn(permissionArrayList);

        List<Permission> permissionList = (List<Permission>) permissionService.getPermissionList();

        assertNotNull(permissionList);

        assertEquals(2, permissionList.size());
        assertTrue(permissionList.contains(new Permission(UUID.fromString("5a753403-d616-4b60-bc45-205ca614b669"), "LOGIN")));
        assertTrue(permissionList.contains(new Permission(UUID.fromString("ada87f83-23a5-48ed-9a84-e55dcd7c6e05"), "VIEW_PROFILE")));
    }

    // getPermissionById

    // getPermissionByKey

}
