package com.giassi.microservice.demo2.rest.users.dtos;

import com.giassi.microservice.demo2.rest.users.entities.Permission;
import com.giassi.microservice.demo2.rest.users.entities.Role;
import com.giassi.microservice.demo2.rest.users.entities.User;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
public class UserDTO implements Serializable {

    public UserDTO() {
        // empty constructor
        roles = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public UserDTO(User user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.surname = user.getSurname();
            this.gender = user.getGender().name();

            this.birthDate = user.getBirthDate();

            this.enabled = user.getEnabled();

            this.note = user.getNote();

            this.creationDt = user.getCreationDt();
            this.updatedDt = user.getUpdatedDt();
            this.loginDt = user.getLoginDt();

            this.secured = user.getSecured();

            // contact, if set
            if (user.getContact() != null) {
                this.contactDTO = new ContactDTO(user.getContact());
            }

            // address, if set
            if (user.getAddress() != null) {
                this.addressDTO = new AddressDTO(user.getAddress());
            }

            // Because the permissions can be associated to more than one roles i'm creating two String arrays
            // with the distinct keys of roles and permissions.
            roles = new ArrayList<>();
            permissions = new ArrayList<>();

            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
                for (Permission p : role.getPermissions()) {
                    String key = p.getPermission();
                    if ((!permissions.contains(key)) && (Optional.of(p).map(Permission::getEnabled).map(enabled -> enabled.equals(Boolean.TRUE)).orElse(false))) {
                        // add the permission only if enabled
                        permissions.add(key);
                    }
                }
            }

        }
    }

    public boolean isEnabled(){
        return Optional.of(getEnabled()).map(enabled -> enabled.equals(Boolean.TRUE)).orElse(false);
    }
    public boolean isSecured(){
        return Optional.of(getSecured()).map(secured -> secured.equals(Boolean.TRUE)).orElse(false);
    }

    private UUID id;
    private String username;
    private String name;
    private String surname;
    private String gender;
    private java.time.LocalDate birthDate;

    private Boolean enabled;

    private String note;

    private LocalDateTime creationDt;
    private LocalDateTime updatedDt;
    private LocalDateTime loginDt;

    private Boolean secured;

    private ContactDTO contactDTO;
    private AddressDTO addressDTO;

    // permissions and roles list
    private List<String> roles;
    private List<String> permissions;

}
