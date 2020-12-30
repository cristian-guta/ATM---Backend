package com.test.demo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "benefit_aud")
@Data
@Accessors(chain = true)
@IdClass(BenefitAuditId.class)
public class BenefitAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Id
    private int rev;
    private int revtype;
    private String user;
    private String Description;

}
