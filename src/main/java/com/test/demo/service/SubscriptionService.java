package com.test.demo.service;

import com.test.demo.dto.ResultDTO;
import com.test.demo.dto.SubscriptionDTO;
import com.test.demo.model.Benefit;
import com.test.demo.model.Client;
import com.test.demo.model.Subscription;
import com.test.demo.repository.BenefitRepository;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private ClientRepository clientRepository;

    private List<Benefit> randomizeBenefits() {
        List<Benefit> benefits = new ArrayList<>();
        benefitRepository.findAll().forEach(benefits::add);

        return benefits;
    }

    public void seedSubscriptions() {
        seedSubscription(1, "Abonament 1", 33.4, randomizeBenefits());
        seedSubscription(2, "Abonament 2", 335.4, randomizeBenefits());

    }

    private void seedSubscription(int id, String name, double price, List<Benefit> benefits) {
        Subscription subscription = subscriptionRepository.getById(id);
        if (subscription == null) {
            subscription = new Subscription()
                    .setId(id)
                    .setName(name)
                    .setPrice(price)
                    .setBenefits(benefits);
            subscriptionRepository.save(subscription);
        }
    }

    public List<SubscriptionDTO> getAllAvailableSubs() {
        List<SubscriptionDTO> allSubscribtions = new ArrayList<>();
        for (Subscription sub : subscriptionRepository.findAll()) {
            SubscriptionDTO subs = new SubscriptionDTO()
                    .setId(sub.getId())
                    .setName(sub.getName())
                    .setPrice(sub.getPrice())
                    .setBenefits(sub.getBenefits());

            allSubscribtions.add(subs);
        }
        return allSubscribtions;
    }

    public SubscriptionDTO getClientSubscription(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        Subscription subscription = client.getSubscription();
        if (subscription != null) {
            SubscriptionDTO sub = new SubscriptionDTO()
                    .setId(subscription.getId())
                    .setName(subscription.getName())
                    .setPrice(subscription.getPrice())
                    .setBenefits(subscription.getBenefits());

            return sub;
        } else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription not found!");
        }


    }

    public ResultDTO activateSubscription(Principal principal, int subId) {

        Client client = clientRepository.findByUsername(principal.getName());
        client.setSubscription(subscriptionRepository.getById(subId));
        clientRepository.save(client);

        return new ResultDTO().setStatus(true).setMessage("Subscription activated!");
    }


    public ResultDTO cancelSubscription(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        client.setSubscription(null);
        clientRepository.save(client);

        return new ResultDTO().setStatus(true).setMessage("Subscription removed from your account!");


    }

    public SubscriptionDTO createSubscription(SubscriptionDTO newSubscription) {
        List<Benefit> benefits = benefitRepository.findByIdIn(newSubscription.getBenefitIds());
        Subscription subscription = new Subscription()
                .setName(newSubscription.getName())
                .setPrice(newSubscription.getPrice())
                .setBenefits(benefits);

        return new SubscriptionDTO(subscriptionRepository.save(subscription));

    }

    public ResultDTO deleteSubscription(int id) {
        Subscription subscription = subscriptionRepository.getById(id);
        subscriptionRepository.delete(subscription);

        return new ResultDTO().setStatus(true).setMessage("Subscription deleted.");
    }

    public SubscriptionDTO updateSubscription(int id, SubscriptionDTO subscriptionDTO) {
        List<Benefit> benefits = benefitRepository.findByIdIn(subscriptionDTO.getBenefitIds());

        Subscription updateSubscription = subscriptionRepository.getById(id);
        updateSubscription.setId(subscriptionDTO.getId())
                .setName(subscriptionDTO.getName())
                .setPrice(subscriptionDTO.getPrice())
                .setBenefits(benefits);
        subscriptionRepository.save(updateSubscription);

        return new SubscriptionDTO(updateSubscription);
    }
}
