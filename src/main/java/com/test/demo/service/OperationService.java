package com.test.demo.service;

import com.test.demo.dto.OperationDTO;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class OperationService {

    private OperationRepository operationRepository;
    private ClientRepository clientRepository;
    private AccountRepository accountRepository;
    private EmailService emailService;
    private Logger log = Logger.getLogger(OperationService.class.getName());

    @Autowired
    public OperationService(OperationRepository operationRepository, ClientRepository clientRepository, AccountRepository accountRepository, EmailService emailService) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.emailService = emailService;
    }

    public OperationDTO findOperationById(String id) {
        log.info("Fetching operation by id...");
        Optional<Operation> optionalOperation = operationRepository.findById(Integer.parseInt(id));
        if (optionalOperation.isPresent()) {
            Operation operation = optionalOperation.get();
            return new OperationDTO(operation);
        } else {
            return new OperationDTO();
        }
    }

    public Page<OperationDTO> getAllOperations(int page, int size, Principal principal) {
        log.info("Listing operations...");
        PageRequest pageRequest = PageRequest.of(page, size);

        Client client = new Client();
        if(clientRepository.findByUsername(principal.getName()) == null){
            client = clientRepository.findClientByEmail(principal.getName());
        }
        else{
            client = clientRepository.findByUsername(principal.getName());
        }

        Page<Operation> pageResult;

        if (!client.getRole().getName().equals("ADMIN")) {
            log.info("User is not admin, fetching personal operations...");
            pageResult = operationRepository.findByClient_Id(client.getId(), pageRequest);
        } else {
            log.info("User is admin, fetching ALL operations...");
            pageResult = operationRepository.findAll(pageRequest);
        }

        List<OperationDTO> operations = pageResult
                .stream()
                .map(OperationDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(operations, pageRequest, pageResult.getTotalElements());

    }

    public OperationDTO createOperation(Principal principal, int accountId, int transferId, String type, Double amount) throws IOException {

        LocalDate date = LocalDate.now();
        Account account = accountRepository.findAccountById(accountId);

        Client client = new Client();
        if(clientRepository.findByUsername(principal.getName()) == null){
            client = clientRepository.findClientByEmail(principal.getName());
        }
        else{
            client = clientRepository.findByUsername(principal.getName());
        }

        Operation operation = new Operation()
                .setAmount(amount)
                .setDate(date)
                .setType(type)
                .setAccount(account)
                .setClient(client);
        if (transferId != 0) {

            Account transfer = accountRepository.findAccountById(transferId);
            operation.setAccount(transfer);
            emailService.createPDF(operation, principal, transfer);
        } else {
            operation.setAccount(account);
            emailService.createPDF(operation, principal, null);
        }

        operationRepository.save(operation);
        return new OperationDTO()
                .setAccount(account)
                .setAmount(amount)
                .setDate(date)
                .setType(type)
                .setClient(client);
    }
}
