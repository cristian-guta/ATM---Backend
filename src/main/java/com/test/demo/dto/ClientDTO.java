package com.test.demo.dto;

import com.test.demo.model.Client;
import com.test.demo.model.Subscription;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientDTO {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String cnp;
    private String address;
    private String email;
    private String password;
    private Boolean deactivated;
    private Subscription subscription;
    private SubscriptionDTO subscriptionDTO;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.username = client.getUsername();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.cnp = client.getCnp();
        this.address = client.getAddress();
        this.email = client.getEmail();
        this.deactivated = client.getStatus();
        this.subscription = client.getSubscription();
    }

    public ClientDTO(String username, String firstName, String lastName, String cnp, String address, String email, SubscriptionDTO subscriptionDTO) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnp = cnp;
        this.address = address;
        this.email = email;
        this.subscriptionDTO = subscriptionDTO;
    }

    public ClientDTO(){}
}
