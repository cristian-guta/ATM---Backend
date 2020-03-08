package com.test.demo.service;

import com.test.demo.dto.ClientDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.model.Client;
import com.test.demo.model.Role;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ClientService(PasswordEncoder bCryptPasswordEncoder, ClientRepository userRepository, RoleRepository roleRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.clientRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void seedClients() {
        seedClient(1,"admin", "Cristian", "Guta", "1234567890", "Adresa 1", "cristian.guta@domain.com", "password", false);
        seedClient(2,"user", " ", " ", " ", "Adresa 2", " ", "password", false);
    }


    private void seedClient(int id, String username, String firstName, String lastName, String cnp, String address, String email, String password, boolean deactivated) {
        Client client = clientRepository.findByUsername(username);
        if (client == null) {
            String roleName = "USER";
            if (username.equals("admin")) {
                roleName = "ADMIN";
            }
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findByName(roleName));
            client = new Client()
                    .setId(id)
                    .setUsername(username)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setCnp(cnp)
                    .setAddress(address)
                    .setEmail(email)
                    .setPassword(bCryptPasswordEncoder.encode(password))
                    .setStatus(deactivated)
                    .setRoles(roles);
            clientRepository.save(client);
        }

    }

    public ClientDTO getCurrentClient(Principal principal) {
        return new ClientDTO((clientRepository.findByUsername(principal.getName())));
    }

    public List<ClientDTO> getAll() {
        List<ClientDTO> clients = new ArrayList<>();
        clientRepository.findAll().forEach(u -> clients.add(new ClientDTO(u)));
        return clients;
    }

    public ClientDTO updateClient(Principal principal, Integer id, ClientDTO updatedClient) {
        Client reqClient = clientRepository.findById(id).get();
        Client currentClient = clientRepository.findByUsername(principal.getName());
        if (reqClient != null && reqClient.getId() == currentClient.getId()) {
            currentClient.setAddress(updatedClient.getAddress());
            currentClient.setFirstName(updatedClient.getFirstName());
            currentClient.setLastName(updatedClient.getLastName());
            currentClient.setEmail(updatedClient.getEmail());

            return new ClientDTO(clientRepository.save(currentClient));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found!");
        }
    }

    public ResultDTO deactivateClient(Integer id) {
        return changeClientStatus(id, true);
    }

    public ResultDTO activateClient(Integer id) {
        return changeClientStatus(id, false);
    }

    private ResultDTO changeClientStatus(Integer id, Boolean status) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            client.get().setStatus(status);
            clientRepository.save(client.get());
            return new ResultDTO().setType("success").setMessage("Successfully changed user status!");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

    }

}
