package com.test.demo.service;

import com.test.demo.dto.AccountDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.model.Account;
import com.test.demo.model.Client;
import com.test.demo.repository.AccountRepository;
import com.test.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    public void seedAccounts() {
        seedAccount(1, 334.5, "Account 1", clientRepository.findByUsername("admin"));
        seedAccount(2, 33345.4, "Account 2", clientRepository.findByUsername("user"));
        seedAccount(3, 33.3, "Account 3", clientRepository.findByUsername("admin"));
    }

    private void seedAccount(int id, Double amount, String name, Client client) {
        Account account = accountRepository.findAccountsByName(name);
        if (account == null) {
            Account newAccount = new Account()
                    .setId(id)
                    .setAmount(amount)
                    .setName(name)
                    .setClient(client);
            accountRepository.save(newAccount);

        }

    }

    public List<AccountDTO> getAccountsByClientCnp(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        List<AccountDTO> accounts = new ArrayList<>();

        accountRepository.findAccountsByClient_Cnp(client.getCnp()).forEach(x -> {
            AccountDTO acc = new AccountDTO()
                    .setAmount(x.getAmount())
                    .setClient(x.getClient())
                    .setName(x.getName());
            accounts.add(acc);
        });

        return accounts.stream().collect(Collectors.toList());
    }

    public AccountDTO createAccount(@RequestBody AccountDTO account, Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        Account newAccount = new Account()
                .setAmount(account.getAmount())
                .setName(account.getName())
                .setClient(client);

        return new AccountDTO(accountRepository.save(newAccount));
    }

    public ResultDTO deleteAccount(int id) {

        Account deleteAccount = accountRepository.findById(id).get();
        accountRepository.delete(deleteAccount);
        return new ResultDTO().setType("success").setMessage("Account deleted.");
    }
}
