package com.test.demo.controller;

import com.test.demo.dto.AccountDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.repository.AccountRepository;
import com.test.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

//    @PreAuthorize("isAuthenticated")
    @GetMapping("/getAccounts")
    public List<AccountDTO> getAccountsByCnp(Principal principal) {
        return accountService.getAccountsByClientCnp(principal);
    }

    @PostMapping("/create")
    public AccountDTO createAccount(@RequestBody AccountDTO newAccount, Principal principal) {
        return accountService.createAccount(newAccount, principal);
    }

    @DeleteMapping("/delete/{id}")
    public ResultDTO deleteAccount(@PathVariable(value = "id") int id) {
        return accountService.deleteAccount(id);
    }
}
