package com.test.demo.controller;

import com.test.demo.dto.ClientDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;


//    @PreAuthorize("hasAdmin()")
    @GetMapping("")
    public List<ClientDTO> getAll() {
        return clientService.getAll();
    }

//    @PreAuthorize("isAuthenticated")
    @GetMapping("/current")
    public ClientDTO getCurrentClient(Principal principal) {
        return clientService.getCurrentClient(principal);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{id}")
    public ClientDTO updateClient(Principal principal, @PathVariable("id") int theId, @RequestBody ClientDTO updatedClient) {

        return clientService.updateClient(principal, theId, updatedClient);
    }

    @GetMapping("/delete/{id}")
    public ResultDTO deleteClient(@PathVariable("id") int theId) {
        return clientService.deactivateClient(theId);
    }


    @PutMapping("/activate/{id}")
    public ResultDTO activateClient(@PathVariable(value = "id") Integer id) {
        return clientService.activateClient(id);
    }

}
