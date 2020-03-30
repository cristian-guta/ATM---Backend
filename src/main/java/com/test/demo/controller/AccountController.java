package com.test.demo.controller;

import com.test.demo.dto.AccountDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    //    @PreAuthorize("isAuthenticated")
    @GetMapping("")
    public List<AccountDTO> getAccountsByCnp(Principal principal) {
        return accountService.getAccountsByClientCnp(principal);
    }

    @GetMapping("/{id}")
    public AccountDTO getAccountById(@PathVariable(value="id") int id){
        return accountService.getAccountById(id);
    }

    //    @PreAuthorize("hasAdmin()")
    @GetMapping("/getAllAccounts")
    public List<AccountDTO> getAllAccounts(Principal principal) {
        return accountService.getAllAccounts(principal);
    }

    @PostMapping("/create")
    public AccountDTO createAccount(@RequestBody AccountDTO newAccount, Principal principal) {
        return accountService.createAccount(newAccount, principal);
    }

    @PutMapping("/update/{id}")
    public AccountDTO updateAccount(@PathVariable(value = "id") int id, @RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(id, accountDTO);
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResultDTO withdrawMoney(@PathVariable(value = "id") int accountId, @PathVariable(value = "amount") Double amount) {
        return accountService.withdrawMoney(accountId, amount);
    }

    @PutMapping("/deposit/{id}/{amount}")
    public ResultDTO depositMoney(@PathVariable(value = "id") int accountId, @PathVariable(value = "amount") Double amount) {
        return accountService.depositMoney(accountId, amount);
    }

    @DeleteMapping("/delete/{id}")
    public ResultDTO deleteAccount(@PathVariable(value = "id") int id) {
        return accountService.deleteAccount(id);
    }

    @PutMapping("/transfer/{senderAccountId}/{receiverAccountId}/{amount}")
    public ResultDTO transferMoney(@PathVariable(value = "senderAccountId") int senderAccountId, @PathVariable(value = "receiverAccountId") int receiverAccountId, @PathVariable(value = "amount") Double amount) {
        return accountService.transferMoney(senderAccountId, receiverAccountId, amount);
    }

}
