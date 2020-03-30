package com.test.demo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @NotNull
    private Double amount;

    @Column
    private String details;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public Account() {
    }

    public Account(int id, String name, Double amount, String details, Client client) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.details = details;
        this.client = client;
    }
}

