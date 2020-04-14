package com.test.demo.dto;

import com.test.demo.model.Account;
import com.test.demo.model.Client;
import com.test.demo.model.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OperationDTO {
    private int id;
    private String type;
    private Double amount;
    private LocalDate date;
    private Client client;
    private ClientDTO clientDTO;
    private Account account;
    private AccountDTO accountDTO;

    public OperationDTO(Operation operation){
        this.id = operation.getId();
        this.type = operation.getType();
        this.amount = operation.getAmount();
        this.date = operation.getDate();
        this.client = operation.getClient();
        this.account = operation.getAccount();
    }

    public OperationDTO(String type, Double amount, LocalDate date, ClientDTO clientDTO, AccountDTO accountDTO){
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.clientDTO = clientDTO;
        this.accountDTO = accountDTO;
    }

}
