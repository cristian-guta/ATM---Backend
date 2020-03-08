package com.test.demo.helper;

import com.test.demo.service.AccountService;
import com.test.demo.service.ClientService;
import com.test.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) {
        roleService.seedRoles();
        clientService.seedClients();
        accountService.seedAccounts();
    }
}
