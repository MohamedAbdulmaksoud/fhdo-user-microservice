package com.giassi.microservice.demo2.rest.users.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="roles")
@Data
@NoArgsConstructor
public class Role {

    public static final UUID USER = UUID.fromString("477ede88-03a8-4702-b8aa-670497771c28");
    public static final UUID ADMINISTRATOR = UUID.fromString("2dd89f24-4d96-47fc-8a62-c15e2228d8aa");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private UUID id;

    @Column(name="role", nullable = false)
    private String role;

    public Role(UUID id, String role) {
        this.id = id;
        this.role = role;
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "permissions_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions= new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role )) return false;
        return id != null && id.equals(((Role) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
