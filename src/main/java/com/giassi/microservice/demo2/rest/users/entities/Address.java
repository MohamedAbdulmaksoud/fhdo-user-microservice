package com.giassi.microservice.demo2.rest.users.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="addresses")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name="address")
    private String address;

    @Column(name="address2")
    private String address2;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="zip_code")
    private String zipCode;

    @OneToOne
    @MapsId
    private User user;

}
