package com.test.demo.dto;

import com.test.demo.model.Account;
import com.test.demo.model.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private int id;
    private Double amount;
    private String name;
    private Client client;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.amount = account.getAmount();
        this.client = account.getClient();
    }

}
