package com.test.demo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "client_aud")
@Data
@Accessors(chain = true)
public class ClientAudit{

    @Id
    private int id;

    private int rev;
    private int revtype;
    private String address;
    private String cnp;
    private String email;
    private String first_name;
    private String last_name;
    private String password;
    private boolean status;
    private String username;
    private int subscription_id;
}
