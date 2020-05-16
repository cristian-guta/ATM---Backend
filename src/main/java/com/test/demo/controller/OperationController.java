package com.test.demo.controller;

import com.test.demo.dto.OperationDTO;
import com.test.demo.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/operations")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @GetMapping("/{page}/{size}")
    public Page<OperationDTO> getAll(@PathVariable(value = "page") int page,
                                     @PathVariable(value = "size") int size,
                                     Principal principal) {
        return operationService.getAllOperations(page, size, principal);
    }

    @GetMapping("/{id}")
    public OperationDTO getOperationById(@PathVariable(value = "id") String id) throws IOException {
        return operationService.findOperationById(id);
    }

    @PostMapping("/create")
    public OperationDTO createOperation(Principal principal, @RequestParam int accountId, @RequestParam int transferId, String type, Double amount) throws IOException {
        return operationService.createOperation(principal, accountId, transferId, type, amount);
    }

}
