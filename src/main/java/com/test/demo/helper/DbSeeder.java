package com.test.demo.helper;

import com.test.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DbSeeder implements CommandLineRunner {

    private AccountService accountService;
    private ClientService clientService;
    private RoleService roleService;
    private SubscriptionService subscriptionService;
    private BenefitService benefitService;
    Logger log = Logger.getLogger(DbSeeder.class.getName());

    @Autowired
    public DbSeeder(AccountService accountService, ClientService clientService, RoleService roleService,
                    SubscriptionService subscriptionService, BenefitService benefitService) {
        this.accountService = accountService;
        this.clientService = clientService;
        this.roleService = roleService;
        this.subscriptionService = subscriptionService;
        this.benefitService = benefitService;
    }

    @Override
    public void run(String... args) {

        log.info("Seedind data into database...");

        benefitService.seedBenefits();
        subscriptionService.seedSubscriptions();
        roleService.seedRoles();
        clientService.seedClients();

        accountService.seedAccounts();
    }
}
