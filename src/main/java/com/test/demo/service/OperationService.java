package com.test.demo.service;

import com.test.demo.dto.OperationDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.model.Account;
import com.test.demo.model.Client;
import com.test.demo.model.Operation;
import com.test.demo.repository.AccountRepository;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OperationService {
    OperationRepository operationRepository;
    ClientRepository clientRepository;
    AccountRepository accountRepository;
    EmailService emailService;

    @Autowired
    public OperationService(OperationRepository operationRepository, ClientRepository clientRepository, AccountRepository accountRepository, EmailService emailService) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.emailService = emailService;
    }

    public OperationDTO findOperationById(String id) {
        Optional<Operation> optionalOperation = operationRepository.findById(Integer.parseInt(id));
        if (optionalOperation.isPresent()) {
            Operation operation = optionalOperation.get();
            return new OperationDTO(operation);
        } else {
            return new OperationDTO();
        }
    }

    public List<OperationDTO> getAllOperations(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        List<OperationDTO> operations = new ArrayList<>();
        operationRepository.getOperationsByClientId(client.getId()).forEach(operation -> {
            OperationDTO op = new OperationDTO()
                    .setId(operation.getId())
                    .setAccount(operation.getAccount())
                    .setAmount(operation.getAmount())
                    .setClient(operation.getClient())
                    .setDate(operation.getDate())
                    .setType(operation.getType());
            operations.add(op);
        });
        return operations;
    }

    public ResultDTO createOperation(Principal principal, int accountId, int transferId, String type, Double amount) throws IOException {
        LocalDate date = LocalDate.now();
        Account account = accountRepository.findAccountById(accountId);
        Account transfer = new Account();
        if (transferId != 0) {
            transfer = accountRepository.findAccountById(transferId);
        }
        Client client = clientRepository.findByUsername(principal.getName());
        Operation operation = new Operation()
                .setAccount(account)
                .setAmount(amount)
                .setDate(date)
                .setType(type)
                .setClient(client);
        if (type.toLowerCase() == "transfer") {
            emailService.createPDF(operation, principal, transfer);
        } else {
            emailService.createPDF(operation, principal, null);
        }
        operationRepository.save(operation);
//        return new OperationDTO()
//                .setAccount(account)
//                .setAmount(amount)
//                .setDate(date)
//                .setType(type)
//                .setClient(client);
        return new ResultDTO().setStatus(true).setMessage("Operation added with success!");
    }
}
