package com.test.demo.controller;

import com.test.demo.dto.AccountDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.service.AccountService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Data
@RestController
@RequestMapping("/api/accounts")
public class AccountController {


    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    @ResponseBody
    public AccountDTO getAccountByClient_id(Principal principal) {
        return accountService.getAccountByClientId(principal);
    }

    @GetMapping("/{id}")
    public AccountDTO getAccountById(@PathVariable(value = "id") int id) {
        return accountService.getAccountById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllAccounts/{page}/{size}")
    public Page<AccountDTO> getAllAccounts(@PathVariable(value = "page") int page,
                                           @PathVariable(value = "size") int size) {
        return accountService.getAllAccounts(page, size);
    }

    @PostMapping(value = "/create")
    public AccountDTO createAccount(@RequestBody AccountDTO newAccount, Principal principal) {
        return accountService.createAccount(newAccount, principal);
    }

    @PutMapping("/update/{id}")
    public AccountDTO updateAccount(@PathVariable(value = "id") int id, @RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(id, accountDTO);
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResultDTO withdrawMoney(Principal principal, @PathVariable(value = "id") int accountId, @PathVariable(value = "amount") Double amount) throws IOException {
        return accountService.withdrawMoney(principal, accountId, amount);
    }

    @PutMapping("/deposit/{id}/{amount}")
    public ResultDTO depositMoney(Principal principal, @PathVariable(value = "id") int accountId, @PathVariable(value = "amount") Double amount) throws IOException {
        return accountService.depositMoney(principal, accountId, amount);
    }

    @DeleteMapping("/delete/{id}")
    public ResultDTO deleteAccount(@PathVariable(value = "id") int id) {
        return accountService.deleteAccount(id);
    }

    @PutMapping("/transfer/{senderAccountId}/{receiverAccountId}/{amount}")
    public ResultDTO transferMoney(Principal principal, @PathVariable(value = "senderAccountId") int senderAccountId, @PathVariable(value = "receiverAccountId") int receiverAccountId, @PathVariable(value = "amount") Double amount) throws IOException {
        return accountService.transferMoney(principal, senderAccountId, receiverAccountId, amount);
    }

}
