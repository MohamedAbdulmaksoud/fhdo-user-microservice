package com.giassi.microservice.demo2.rest.users.services;

import com.giassi.microservice.demo2.rest.users.entities.Permission;
import com.giassi.microservice.demo2.rest.users.entities.Role;
import com.giassi.microservice.demo2.rest.users.exceptions.*;
import com.giassi.microservice.demo2.rest.users.repositories.PermissionRepository;
import com.giassi.microservice.demo2.rest.users.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private RoleService roleService = new RoleService();

    @Test
    public void givenWrongRoleId_whenGetRoleById_throwInvalidRoleIdentifierException() {
        assertThrows(InvalidRoleIdentifierException.class, () -> roleService.getRoleById(null));
    }

    @Test
    public void givenNotExistingRoleId_whenGetRoleById_throwRoleNotFoundException() {
        UUID roleId = UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28");
        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleById(roleId));
    }

    @Test
    public void given_existing_roleId_when_getRoleById_return_Role() {
        UUID roleId = UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28");
        Role role = new Role(roleId, "TEST ROLE");

        given(roleRepository.findById(roleId)).willReturn(Optional.of(role));

        Role returnRole = roleService.getRoleById(roleId);

        assertNotNull(returnRole);
        assertEquals(role.getId(), returnRole.getId());
    }

    // validateRoleName

    @Test
    public void givenInvalidRoleName_whenValidateRoleName_throwInvalidRoleDataException() {
        assertThrows(InvalidRoleDataException.class, () -> RoleService.validateRoleName(null));
    }

    @Test
    public void given_empty_role_name_when_validateRoleName_throw_InvalidRoleDataException() {
        assertThrows(InvalidRoleDataException.class, () -> RoleService.validateRoleName(""));
    }

    @Test
    public void given_empty_role_name_when_validateRoleName_no_exception_occurs() {
        RoleService.validateRoleName("VALID_ROLE_TEST");
    }

    // createRole

    @Test
    public void given_invalid_role_name_when_createRole_throw_InvalidRoleDataException() {
        assertThrows(InvalidRoleDataException.class, () -> roleService.createRole(null));
    }

    @Test
    public void given_valid_used_name_when_createRole_throw_RoleInUseException() {
        Role role = new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST");
        given(roleRepository.findByRole("TEST")).willReturn(Optional.of(role));
        assertThrows(RoleInUseException.class, () -> roleService.createRole("TEST"));

    }

    @Test
    public void given_valid_not_used_name_when_createRole_returnRole() {
        UUID genId = UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28");
        Role roleData = new Role(genId, "TEST");

        when(roleRepository.save(any(Role.class))).thenReturn(new Role(genId, roleData.getRole()));

        Role role = roleService.createRole("TEST");

        assertNotNull(role);
        assertEquals(genId, role.getId());
        assertEquals("TEST", role.getRole());
    }

    // deleteRole

    @Test
    public void given_not_existing_role_when_deleteRole_throw_RoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> roleService.deleteRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28")));
    }

    @Test
    public void given_existing_role_in_use_when_deleteRole_throw_RoleInUseException() {
        given(roleRepository.findById(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(Optional.of(new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST")));
        given(roleRepository.countRoleUsage(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(10L);

        assertThrows(RoleInUseException.class, () -> roleService.deleteRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28")));
    }

    @Test
    public void given_existing_role_not_in_use_when_deleteRole_Ok() {
        given(roleRepository.findById(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(Optional.of(new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST")));
        given(roleRepository.countRoleUsage(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(0L);

        roleService.deleteRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"));
    }

    // validatePermissionKey

    @Test
    public void givenNullPermissionKey_whenValidatePermissionKey_throwInvalidPermissionDataException() {
        assertThrows(InvalidPermissionDataException.class, () -> RoleService.validatePermissionKey(null));
    }

    @Test
    public void givenEmptyPermissionKey_whenValidatePermissionKey_throwInvalidPermissionDataException() {
        assertThrows(InvalidPermissionDataException.class, () -> RoleService.validatePermissionKey(""));
    }

    // addPermissionOnRole

    @Test
    public void givenInvalidPermission_whenAddPermissionOnRole_throwInvalidPermissionDataException() {
        assertThrows(InvalidPermissionDataException.class, () -> roleService.addPermissionOnRole(UUID.fromString("5a753403-d616-4b60-bc45-205ca614b669"), ""));
    }

    @Test
    public void givenNotExistingRole_whenAddPermissionOnRole_throwRoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> roleService.addPermissionOnRole(UUID.fromString("5a753403-d616-4b60-bc45-205ca614b669"), "PERMISSION_ONE"));
    }

    @Test
    public void given_existing_role_and_not_existing_permission_return_role_updated() {
        Role role = new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST");
        given(roleRepository.findById(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(Optional.of(role));

        Role roleUpdated = roleService.addPermissionOnRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION_ONE");

        assertNotNull(roleUpdated);
        // role data
        assertEquals(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), roleUpdated.getId());
        assertEquals("TEST", roleUpdated.getRole());

        // permissions
        assertEquals(1L, roleUpdated.getPermissions().size());
    }

    @Test
    public void given_existing_role_and_existing_permission_return_role_updated() {
        Role role = new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST");
        given(roleRepository.findById(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(Optional.of(role));

        Permission permission = new Permission();
        permission.setId(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"));
        permission.setPermission("PERMISSION_ONE");

        given(permissionRepository.findByPermission("PERMISSION_ONE")).willReturn(Optional.of(permission));

        Role roleUpdated = roleService.addPermissionOnRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION_ONE");

        assertNotNull(roleUpdated);
        // role data
        assertEquals(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), roleUpdated.getId());
        assertEquals("TEST", roleUpdated.getRole());

        // permissions
        assertEquals(1L, roleUpdated.getPermissions().size());
    }

    @Test
    public void givenExistingRoleAndExistingAlreadyAssociatedPermission_throwInvalidPermissionDataException() {
        Role role = new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST");
        role.getPermissions().add(new Permission(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION_ONE"));

        given(roleRepository.findById(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(Optional.of(role));

        Permission permission = new Permission(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION_ONE");

        given(permissionRepository.findByPermission("PERMISSION_ONE")).willReturn(Optional.of(permission));

        assertThrows(InvalidPermissionDataException.class, () -> {
            Role roleUpdated = roleService.addPermissionOnRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION_ONE");

            assertNotNull(roleUpdated);
            // role data
            assertEquals(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), roleUpdated.getId());
            assertEquals("TEST", roleUpdated.getRole());

            // permissions
            assertEquals(1L, roleUpdated.getPermissions().size());
        });
    }

    // removePermissionOnRole

    @Test
    public void givenNotValidPermission_whenRemovePermissionOnRole_throwInvalidPermissionDataException() {
        assertThrows(InvalidPermissionDataException.class, () -> roleService.removePermissionOnRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), ""));
    }

    @Test
    public void givenNotExistingRole_whenRemovePermissionOnRole_throwRoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> roleService.removePermissionOnRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION"));
    }

    @Test
    public void givenExistingRoleNotExistingPermission_whenRemovePermissionOnRole_throwPermissionNotFoundException() {
        Role role = new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST");
        given(roleRepository.findById(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(Optional.of(role));

        assertThrows(PermissionNotFoundException.class, () -> roleService.removePermissionOnRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION"));
    }

    @Test
    public void given_existing_role_existing_permission_not_used_when_removePermissionOnRole_return_Role() {
        Role role = new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST");
        given(roleRepository.findById(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(Optional.of(role));

        Permission permission = new Permission();
        permission.setId(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"));
        permission.setPermission("PERMISSION");

        given(permissionRepository.findByPermission("PERMISSION")).willReturn(Optional.of(permission));

        Role roleUpdated = roleService.removePermissionOnRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION");

        assertNotNull(roleUpdated);
        // role data
        assertEquals(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), roleUpdated.getId());
        assertEquals("TEST", roleUpdated.getRole());

        // permissions
        assertEquals(0L, roleUpdated.getPermissions().size());
    }

    @Test
    public void given_existing_role_existing_permission_in_used_when_removePermissionOnRole_return_Role() {
        Role role = new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "TEST");
        given(roleRepository.findById(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"))).willReturn(Optional.of(role));

        Permission permission = new Permission();
        permission.setId(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"));
        permission.setPermission("PERMISSION");

        given(permissionRepository.findByPermission("PERMISSION")).willReturn(Optional.of(permission));

        Role roleUpdated = roleService.removePermissionOnRole(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "PERMISSION");

        assertNotNull(roleUpdated);
        // role data
        assertEquals(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), roleUpdated.getId());
        assertEquals("TEST", roleUpdated.getRole());

        // permissions
        assertEquals(0L, roleUpdated.getPermissions().size());
    }

    // getRoleList

    @Test
    public void calling_getRoleList_then_return_list_of_roles() {
        ArrayList<Role> roleArrayList = new ArrayList<>();
        roleArrayList.add(new Role(UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28"), "FIRST_ROLE"));
        roleArrayList.add(new Role(UUID.fromString("ada87f83-23a5-48ed-9a84-e55dcd7c6e05"), "SECOND_ROLE"));

        given(roleRepository.findAll()).willReturn(roleArrayList);

        Iterable<Role> roleIterable = roleService.getRoleList();

        assertNotNull(roleIterable);

        // TODO: check on size and or data
    }

}
