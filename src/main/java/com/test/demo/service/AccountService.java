package com.test.demo.service;

import com.test.demo.dto.AccountDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.model.Account;
import com.test.demo.model.Client;
import com.test.demo.model.Operation;
import com.test.demo.repository.AccountRepository;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private ClientRepository clientRepository;
    private OperationService operationService;
    private OperationRepository operationRepository;
    private Logger log = Logger.getLogger(AccountService.class.getName());

    @Autowired
    public AccountService(AccountRepository accountRepository, ClientRepository clientRepository, OperationService operationService, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.operationService = operationService;
        this.operationRepository = operationRepository;
    }

    public void seedAccounts() {
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

    public List<Account> getAllAccountsUnpaged(){
        return accountRepository.findAll();
    }

    public Page<AccountDTO> getAllAccounts(int page, int size) {

        log.info("Listing ALL accounts...");
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Account> pageResult = accountRepository.findAll(pageRequest);

        List<AccountDTO> accounts = pageResult.stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(accounts, pageRequest, pageResult.getTotalElements());
    }


    public AccountDTO getAccountByClientId(Principal principal) {
        log.info("Listing client's account based on his ID...");

        Client client = new Client();
        if(clientRepository.findByUsername(principal.getName()) == null){
            client = clientRepository.findClientByEmail(principal.getName());
        }
        else{
            client = clientRepository.findByUsername(principal.getName());
        }

        return accountRepository.findAccountByClient_Id(client.getId());
    }

    public AccountDTO createAccount(@RequestBody AccountDTO account, Principal principal) {
        log.info("Creating account...");

        Client client = new Client();
        if(clientRepository.findByUsername(principal.getName()) == null){
            client = clientRepository.findClientByEmail(principal.getName());
        }
        else{
            client = clientRepository.findByUsername(principal.getName());
        }

        Account newAccount = new Account()
                .setAmount(account.getAmount())
                .setName(account.getName())
                .setDetails(account.getDetails())
                .setClient(client);

        log.info("Account created...");
        return new AccountDTO(accountRepository.save(newAccount));
    }

    public ResultDTO deleteAccount(int id) {

        log.info("Deleting account...");

        Account deleteAccount = accountRepository.findAccountById(id);

        if (deleteAccount != null) {
            Client client = deleteAccount.getClient();
            AccountDTO acc = accountRepository.findAccountByClient_Id(client.getId());
            Account account = new Account()
                    .setId(acc.getId())
                    .setAmount(acc.getAmount())
                    .setClient(acc.getClient())
                    .setDetails(acc.getDetails());
            account.setClient(null);
//            List<Operation> operations = operationRepository.getOperationsByClientId(client.getId());
//            for (Operation op: operations
//                 ) {
//                operationRepository.delete(op);
//            }
            accountRepository.deleteAccountById(id);

            log.info("Account deleted...");
            return new ResultDTO().setStatus(true).setMessage("Account deleted!");
        } else {
            log.info("Something went wrong while executing deleteAccount(...) method...");
//            return new ResultDTO().setStatus(false).setMessage("No account with this id found!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!");
        }
    }

    public AccountDTO updateAccount(int id, AccountDTO accountDTO) {
        log.info("Updating account's informations...");

        Account updateAccount = accountRepository.findAccountById(id);
        updateAccount.setId(accountDTO.getId())
                .setName(accountDTO.getName())
                .setAmount(accountDTO.getAmount())
                .setDetails(accountDTO.getDetails());
        accountRepository.save(updateAccount);

        log.info("Account updated...");
        return new AccountDTO(updateAccount);
    }

    public ResultDTO depositMoney(Principal principal, int accountId, Double amount) throws IOException {
        log.info("Depositing money into account...");

        Account account = accountRepository.findAccountById(accountId);
        Double total = account.getAmount() + amount;
        account.setAmount(total);

        log.info("Saving new account state...");
        accountRepository.save(account);

        log.info("Creating new operation and preparing mail summary...");
        operationService.createOperation(principal, account.getId(), 0, "deposit", amount);
        return new ResultDTO().setStatus(true).setMessage("Money deposed!");
    }

    public ResultDTO withdrawMoney(Principal principal, int accountId, Double amount) throws IOException {
        log.info("Withdrawing money...");

        Account account = accountRepository.findAccountById(accountId);
        try {
            Double total = account.getAmount() - amount;
            if (total < 0) {
                throw new RuntimeException("You want to withdraw more than you own! Throwing exception...");
            } else {
                account.setAmount(total);
                log.info("Saving new account state...");
                accountRepository.save(account);

                log.info("Creating operation and preparing mail summary...");
                operationService.createOperation(principal, account.getId(), 0, "deposit", amount);
            }
        } catch (RuntimeException exc) {
            exc.printStackTrace();
        }
        return new ResultDTO().setStatus(true).setMessage("Money deposed!");
    }

    public ResultDTO transferMoney(Principal principal, int senderAccountId, int receiverAccountId, Double amount) throws IOException {
        log.info("Transfering money...");

        Account account = accountRepository.findAccountById(senderAccountId);
        Account toSendTo = accountRepository.findAccountById(receiverAccountId);

        try {
            if (amount < account.getAmount()) {
                Double senderAmount = account.getAmount() - amount;
                account.setAmount(senderAmount);

                log.info("Saving new state of sender's account...");
                accountRepository.save(account);

                Double receiverAmount = toSendTo.getAmount() + amount;
                toSendTo.setAmount(receiverAmount);

                log.info("Saving new state of receiver's account...");
                accountRepository.save(toSendTo);

                log.info("Creating operation and preparing mail summary...");
                operationService.createOperation(principal, account.getId(), toSendTo.getId(), "transfer", amount);
            } else {
                throw new RuntimeException("You want to transfer more than you own! Throwing exception...");
            }
        } catch (RuntimeException exc) {
            exc.printStackTrace();
        }

        return new ResultDTO().setStatus(true).setMessage("Amount successfully transfered!");
    }

    public AccountDTO getAccountById(int id) {
        log.info("Listing account by id...");

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
            log.info("Exception while listing account by id...");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!");
        }
    }
}
