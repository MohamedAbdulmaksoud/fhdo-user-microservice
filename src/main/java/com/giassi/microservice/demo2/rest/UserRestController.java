package com.giassi.microservice.demo2.rest;

import com.giassi.microservice.demo2.rest.users.dtos.UserDTO;
import com.giassi.microservice.demo2.rest.users.dtos.UserListDTO;
import com.giassi.microservice.demo2.rest.users.dtos.requests.CreateOrUpdateUserDTO;
import com.giassi.microservice.demo2.rest.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserListDTO> getUserPresentationList() {
        List<UserDTO> list = userService.getUserPresentationList();
        UserListDTO userListDTO = new UserListDTO();
        list.stream().forEach(e -> userListDTO.getUserList().add(e));
        return ResponseEntity.ok(userListDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateOrUpdateUserDTO createOrUpdateUserDTO) {
        return new ResponseEntity(new UserDTO(userService.createUser(createOrUpdateUserDTO)), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") UUID id) {
        return new UserDTO(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID id, @RequestBody CreateOrUpdateUserDTO updateUserDTO) {
        return new ResponseEntity(new UserDTO(userService.updateUser(id, updateUserDTO)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // add or remove a Role on a user
    @PostMapping("/{id}/roles/{roleId}")
    public ResponseEntity<UserDTO> addRole(@PathVariable("id") UUID id, @PathVariable("roleId") UUID roleId) {
        return new ResponseEntity(new UserDTO(userService.addRole(id, roleId)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    public ResponseEntity<UserDTO> removeRole(@PathVariable("id") UUID id, @PathVariable("roleId") UUID roleId) {
        return new ResponseEntity(new UserDTO(userService.removeRole(id, roleId)), null, HttpStatus.OK);
    }

}
