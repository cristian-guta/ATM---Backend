package com.test.demo.service;

import com.test.demo.dto.AccountDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.model.Account;
import com.test.demo.model.Client;
import com.test.demo.repository.AccountRepository;
import com.test.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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

    @Autowired
    private OperationService operationService;

    public void seedAccounts() {
        seedAccount(1, 334.5, "Account 1", "detail 1", /*clientRepository.findByUsername("admin")*/ clientRepository.findByUsername("user1"));
        seedAccount(2, 33345.4, "Account 2", "detail 2", clientRepository.findByUsername("user"));
        seedAccount(3, 33.3, "Account 3", "detail 3", clientRepository.findByUsername("user1"));
    }

    private void seedAccount(int id, Double amount, String name, String details, Client client) {
        Account account = accountRepository.findAccountsByName(name);
        if (account == null) {
            Account newAccount = new Account()
                    .setId(id)
                    .setAmount(amount)
                    .setName(name)
                    .setDetails(details)
                    .setClient(client);
            accountRepository.save(newAccount);
        }
    }

    public List<AccountDTO> getAccountsByClientCnp(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        List<AccountDTO> accounts = new ArrayList<>();

        accountRepository.findAccountsByClient_Cnp(client.getCnp()).forEach(x -> {
            AccountDTO acc = new AccountDTO()
                    .setId(x.getId())
                    .setAmount(x.getAmount())
                    .setClient(x.getClient())
                    .setName(x.getName())
                    .setDetails(x.getDetails());
            accounts.add(acc);
        });

        return accounts.stream().collect(Collectors.toList());
    }

    public AccountDTO createAccount(@RequestBody AccountDTO account, Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        Account newAccount = new Account()

                .setAmount(account.getAmount())
                .setName(account.getName())
                .setDetails(account.getDetails())
                .setClient(client);

        return new AccountDTO(accountRepository.save(newAccount));
    }

    public ResultDTO deleteAccount(int id) {

        Account deleteAccount = accountRepository.findAccountById(id);
        if (deleteAccount != null) {
            accountRepository.deleteAccountById(id);
            return new ResultDTO().setStatus(true).setMessage("Account deleted!");
        } else {
            return new ResultDTO().setStatus(false).setMessage("No account with this id found!");
        }
    }

    public List<AccountDTO> getAllAccounts(Principal principal) {
        List<AccountDTO> accounts = new ArrayList<>();
        accountRepository.findAll().forEach(account -> {
            AccountDTO acc = new AccountDTO()
                    .setId(account.getId())
                    .setName(account.getName())
                    .setAmount(account.getAmount())
                    .setDetails(account.getDetails())
                    .setClient(account.getClient());
            accounts.add(acc);
        });
        return accounts;
    }

    public AccountDTO updateAccount(int id, AccountDTO accountDTO) {

        Account updateAccount = accountRepository.findAccountById(id);
        updateAccount.setId(accountDTO.getId())
                .setName(accountDTO.getName())
                .setAmount(accountDTO.getAmount())
                .setDetails(accountDTO.getDetails());
        accountRepository.save(updateAccount);

        return new AccountDTO(updateAccount);
    }

    public ResultDTO depositMoney(Principal principal, int accountId, Double amount) throws IOException {
        Account account = accountRepository.findAccountById(accountId);
        Double total = account.getAmount() + amount;
        account.setAmount(total);
        accountRepository.save(account);
        operationService.createOperation(principal, account.getId(), 0, "deposit", amount);
        return new ResultDTO().setStatus(true).setMessage("Money deposed!");
    }

    public ResultDTO withdrawMoney(Principal principal, int accountId, Double amount) throws IOException {
        Account account = accountRepository.findAccountById(accountId);
        Double total = account.getAmount() - amount;
        account.setAmount(total);
        accountRepository.save(account);
        operationService.createOperation(principal, account.getId(), 0, "deposit", amount);
        return new ResultDTO().setStatus(true).setMessage("Money deposed!");
    }

    public ResultDTO transferMoney(Principal principal, int senderAccountId, int receiverAccountId, Double amount) throws IOException {
        Account account = accountRepository.findAccountById(senderAccountId);
        Account toSendTo = accountRepository.findAccountById(receiverAccountId);

        Double senderAmount = account.getAmount() - amount;
        account.setAmount(senderAmount);
        accountRepository.save(account);

        Double receiverAmount = toSendTo.getAmount() + amount;
        toSendTo.setAmount(receiverAmount);
        accountRepository.save(toSendTo);
        operationService.createOperation(principal, account.getId(), toSendTo.getId(), "transfer", amount);
        return new ResultDTO().setStatus(true).setMessage("Amount successfully transfered!");
    }

    public AccountDTO getAccountById(int id) {
        Account account = accountRepository.findAccountById(id);
        if (account != null) {
            AccountDTO acc = new AccountDTO()
                    .setId(account.getId())
                    .setDetails(account.getDetails())
                    .setClient(account.getClient())
                    .setAmount(account.getAmount())
                    .setName(account.getName());

            return acc;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!");
        }
    }
}
