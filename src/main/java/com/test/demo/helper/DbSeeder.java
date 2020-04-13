package com.test.demo.helper;

import com.test.demo.service.*;
import org.junit.Test;
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

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private BenefitService benefitService;

    @Override
    public void run(String... args) {
        benefitService.seedBenefits();
        subscriptionService.seedSubscriptions();
        roleService.seedRoles();
        clientService.seedClients();

        accountService.seedAccounts();
    }
}
