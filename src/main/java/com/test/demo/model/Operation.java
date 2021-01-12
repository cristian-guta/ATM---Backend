package com.test.demo.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "operations")
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String type;

    @Column
    private Double amount;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/mm/dd")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Account account;

    public Operation() {
    }

    public Operation(int id, String type, Double amount, Client client, Account account) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.client = client;
        this.account = account;
    }
}
