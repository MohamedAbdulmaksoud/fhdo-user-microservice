package com.giassi.microservice.demo2.rest.users.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    public Permission(UUID id, String permission) {
        this.id = id;
        this.permission = permission;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private UUID id;

    @Column(name="permission", nullable = false)
    private String permission;

    // enabled as default
    @Column(name="enabled")
    private Boolean enabled = true;

    @Column(name="note")
    private String note;

}
