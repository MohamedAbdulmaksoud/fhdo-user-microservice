package com.giassi.microservice.demo2.rest.users.repositories;

import com.giassi.microservice.demo2.rest.users.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    @Query(value = "SELECT u FROM User u WHERE u.contact.email = :email")
    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT u FROM User u WHERE u.username = lower(:username)")
    User findByUsername(String username);

}
