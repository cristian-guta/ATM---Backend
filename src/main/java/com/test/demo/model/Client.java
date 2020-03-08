package com.test.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "client", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "cnp"})})
@AllArgsConstructor
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String cnp;

    private String address;

    @NotNull
    private String email;

    @JsonIgnore
    private String password;

    @NotNull
    private Boolean status = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "client_roles", joinColumns = {
            @JoinColumn(name = "client_id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    private List<Role> roles;

    public Client() {
    }

}
