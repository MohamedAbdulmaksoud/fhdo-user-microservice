package com.giassi.microservice.demo2.rest.users.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private UUID id;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="surname", nullable = false)
    private String surname;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private Gender gender;

    // Birth date without a time-zone in the ISO-8601 calendar system, such as 2007-12-03
    @Column(name = "birth_date")
    private java.time.LocalDate birthDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Contact contact;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Address address;

    @Column(name="enabled")
    private Boolean enabled;

    @Column(name="note")
    private String note;

    @Basic
    private java.time.LocalDateTime creationDt;

    @Basic
    private java.time.LocalDateTime updatedDt;

    @Basic
    private java.time.LocalDateTime loginDt;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "users_roles",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name="secured")
    private Boolean secured;

}
