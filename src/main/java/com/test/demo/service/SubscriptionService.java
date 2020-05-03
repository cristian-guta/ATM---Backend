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
import java.util.logging.Logger;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;
    private BenefitRepository benefitRepository;
    private ClientRepository clientRepository;
    private Logger log = Logger.getLogger(SubscriptionService.class.getName());

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, BenefitRepository benefitRepository, ClientRepository clientRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.benefitRepository = benefitRepository;
        this.clientRepository = clientRepository;
    }

    private List<Benefit> randomizeBenefits() {
        List<Benefit> benefits = new ArrayList<>();
        benefitRepository.findAll().forEach(benefits::add);

        return benefits;
    }

    public void seedSubscriptions() {
        seedSubscription(1, "Abonament 1", 33.4, randomizeBenefits(), false);
        seedSubscription(2, "Abonament 2", 335.4, randomizeBenefits(), false);

    }

    private void seedSubscription(int id, String name, double price, List<Benefit> benefits, Boolean deleted) {
        Subscription subscription = subscriptionRepository.getById(id);
        if (subscription == null) {
            subscription = new Subscription()
                    .setId(id)
                    .setName(name)
                    .setPrice(price)
                    .setDeleted(deleted)
                    .setBenefits(benefits);
            subscriptionRepository.save(subscription);
        }
    }

    public List<SubscriptionDTO> getAllAvailableSubs() {
        log.info("Fetching all available subscriptions...");

        List<SubscriptionDTO> allSubscribtions = new ArrayList<>();
        for (Subscription sub : subscriptionRepository.findAll()) {
            SubscriptionDTO subs = new SubscriptionDTO()
                    .setId(sub.getId())
                    .setName(sub.getName())
                    .setPrice(sub.getPrice())
                    .setDeleted(sub.getDeleted())
                    .setBenefits(sub.getBenefits());
            if (!subs.getDeleted()) {
                allSubscribtions.add(subs);
            }
        }
        return allSubscribtions;
    }

    public SubscriptionDTO getClientSubscription(Principal principal) {
        log.info("Fetching client's subscription");

        Client client = clientRepository.findByUsername(principal.getName());
        Subscription subscription = client.getSubscription();
        if (subscription != null && client.getUsername() != "admin") {
            SubscriptionDTO sub = new SubscriptionDTO()
                    .setId(subscription.getId())
                    .setName(subscription.getName())
                    .setPrice(subscription.getPrice())
                    .setBenefits(subscription.getBenefits());

            return sub;
        } else {
            log.info("Something went wrong while executing getClientSubscription(...) method...");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription not found or current user is admin!");
        }
    }

    public ResultDTO activateSubscription(Principal principal, int subId) {
        log.info("Activating subscription for " + principal.getName() + "...");

        Client client = clientRepository.findByUsername(principal.getName());
        client.setSubscription(subscriptionRepository.getById(subId));
        clientRepository.save(client);

        log.info("Subscription activated...");
        return new ResultDTO().setStatus(true).setMessage("Subscription activated!");
    }


    public ResultDTO cancelSubscription(Principal principal) {
        log.info("Canceling subscription for ..." + principal.getName() + "...");

        Client client = clientRepository.findByUsername(principal.getName());
        client.setSubscription(null);
        clientRepository.save(client);

        log.info("Subscription canceled...");
        return new ResultDTO().setStatus(true).setMessage("Subscription removed from your account!");
    }

    public SubscriptionDTO createSubscription(SubscriptionDTO newSubscription) {
        log.info("Creating new subscription...");

        List<Benefit> benefits = benefitRepository.findByIdIn(newSubscription.getBenefitIds());
        Subscription subscription = new Subscription()
                .setName(newSubscription.getName())
                .setPrice(newSubscription.getPrice())
                .setBenefits(benefits);

        log.info("Saving new subscription...");
        return new SubscriptionDTO(subscriptionRepository.save(subscription));

    }

    //deleting subscription and letting it active for users who are still subscribed to it
    public ResultDTO deleteSubscription(int id, Principal principal) {
        log.info("Deleting subscription...");

        Subscription subscription = subscriptionRepository.getById(id);
        subscription.setDeleted(true);

        subscriptionRepository.save(subscription);

        log.info("Subscription deleted...");
        return new ResultDTO().setStatus(true).setMessage("Subscription deleted.");
    }

//deleting subscription and automatically deactivating it from the users subscribed to it

//    public ResultDTO deleteSubscription(int id, Principal principal) {
//
//        Subscription subscription = subscriptionRepository.getById(id);
//        for (Client client : clientRepository.findAll()) {
//            if(!client.getUsername().equals("admin")) {
//                if (client.getSubscription().getId() == subscription.getId()) {
//                    client.setSubscription(null);
//                    clientRepository.save(client);
//                }
//            }
//        }
//        subscription.setDeleted(true);
//
//        subscriptionRepository.save(subscription);
//        return new ResultDTO().setStatus(true).setMessage("Subscription deleted.");
//    }

    public SubscriptionDTO updateSubscription(int id, SubscriptionDTO subscriptionDTO) {
        log.info("Updating subscription...");

        List<Benefit> benefits = benefitRepository.findByIdIn(subscriptionDTO.getBenefitIds());

        Subscription updateSubscription = subscriptionRepository.getById(id);
        updateSubscription.setId(subscriptionDTO.getId())
                .setName(subscriptionDTO.getName())
                .setPrice(subscriptionDTO.getPrice())
                .setBenefits(benefits);

        log.info("Saving new subscription object state...");
        subscriptionRepository.save(updateSubscription);

        log.info("Subscription updated...");
        return new SubscriptionDTO(updateSubscription);
    }
}
