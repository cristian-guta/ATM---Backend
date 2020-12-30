package com.test.demo.model;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "subscription_aud")
@Data
@Accessors(chain = true)
@IdClass(SubscriptionAuditId.class)
public class SubscriptionAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Id
    private int rev;

    private int revtype;
    private String name;
    private Double price;
    private String user;
}
