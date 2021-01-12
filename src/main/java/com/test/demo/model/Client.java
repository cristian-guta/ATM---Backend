package com.test.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "client", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "cnp"})})
@AllArgsConstructor
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //    @NotNull
    private String username;

    //    @NotNull
    private String firstName;

    //    @NotNull
    private String lastName;

//    @NotNull
//    private String name;

    //    @NotNull
    private String cnp;

    private String address;

    @NotNull
    private String email;

    //    @JsonIgnore
    private String password;

    //    @NotNull
    private Boolean status = false;

    private boolean hasUpdated = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Subscription subscription;

    public Client() {
    }

}
