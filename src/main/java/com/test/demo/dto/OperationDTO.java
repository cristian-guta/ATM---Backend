package com.test.demo.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.test.demo.model.Account;
import com.test.demo.model.Client;
import com.test.demo.model.Operation;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OperationDTO {
    private int id;
    private String type;
    private Double amount;
    private LocalDate date;
    private Client client;
    private Account account;

    public OperationDTO(Operation operation) {
        this.id = operation.getId();
        this.type = operation.getType();
        this.amount = operation.getAmount();
        this.date = operation.getDate();
        this.client = operation.getClient();
        this.account = operation.getAccount();
    }

    public OperationDTO(String type, Double amount, LocalDate date, Client client, Account account) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.client = client;
        this.account = account;
    }

}
