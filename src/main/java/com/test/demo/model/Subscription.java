package com.test.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Audited
@Data
@Accessors(chain = true)
@Entity
@Table(name = "subscription", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private Double price;

//    @NotNull
//    private Boolean deleted;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subscriptions_benefits", joinColumns = {
            @JoinColumn(name = "subscription_id")}, inverseJoinColumns = {
            @JoinColumn(name = "benefit_id")
    })
    @NotAudited
    private List<Benefit> benefits;

    public Subscription() {
    }

}
