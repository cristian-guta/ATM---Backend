package com.test.demo.service;

import com.test.demo.dto.ClientDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.model.Client;
import com.test.demo.model.Role;
import com.test.demo.model.Subscription;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.RoleRepository;
import com.test.demo.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ClientService {

    private ClientRepository clientRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder bCryptPasswordEncoder;
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    ClientService(PasswordEncoder bCryptPasswordEncoder, ClientRepository userRepository, RoleRepository roleRepository, SubscriptionRepository subscriptionRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.clientRepository = userRepository;
        this.roleRepository = roleRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Subscription> getRandomElement(List<Subscription> list, int totalItems) {
        Random rand = new Random();

        List<Subscription> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            int randomIndex = rand.nextInt(list.size());
            newList.add(list.get(randomIndex));
        }
        return newList;
    }

    private List<Subscription> randomizeSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptionRepository.findAll().forEach(subscriptions::add);

        return subscriptions;
    }

    public void seedClients() {
        seedClient(1, "admin", "Cristian", "Guta", "1234567890", "Adresa 1", "cristian.guta@domain.com", "password", false, subscriptionRepository.getById(1));
        seedClient(2, "user", " ", " ", " ", "Adresa 2", " ", "password", false, subscriptionRepository.getById(2));
        seedClient(3, "user1", "", "", " ", "Adresa 3", "", "parola", false, subscriptionRepository.getById(2));
    }


    private void seedClient(int id, String username, String firstName, String lastName, String cnp, String address, String email, String password, boolean deactivated, Subscription subscription) {
        Client client = clientRepository.findByUsername(username);
        if (client == null) {
            String roleName = "USER";
            if (username.equals("admin")) {
                roleName = "ADMIN";
            }

            Role role = roleRepository.findByName(roleName);
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
                    .setRole(role)
                    .setSubscription(subscription);
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
            currentClient.setCnp(updatedClient.getCnp());

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
