package com.giassi.microservice.demo2.rest.users.repositories;

import com.giassi.microservice.demo2.rest.users.entities.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, UUID> {

    Optional<Permission> findByPermission(String permission);

    @Query(value = "select count(*) from permissions_roles where permission_id = ?1", nativeQuery = true)
    Long countPermissionUsage(UUID permissionId);

    void deleteByPermission(String permission);

}
