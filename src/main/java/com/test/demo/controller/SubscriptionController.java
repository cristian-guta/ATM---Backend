package com.test.demo.controller;

import com.test.demo.dto.ResultDTO;
import com.test.demo.dto.SubscriptionDTO;
import com.test.demo.service.SubscriptionService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Data
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {


    private SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService){
        this.subscriptionService = subscriptionService;
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public List<SubscriptionDTO> getAllAvailableSubscriptions() {
        return subscriptionService.getAllAvailableSubs();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getSubscription")
    public SubscriptionDTO getCurrentSubscription(Principal principal) {
        return subscriptionService.getClientSubscription(principal);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/activateSubscription/{id}")
    public ResultDTO activateSubscription(Principal principal, @PathVariable(value = "id") int id) throws IOException {
        return subscriptionService.activateSubscription(principal, id);
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/cancelSubscription")
    public ResultDTO cancelSubscription(Principal principal) {
        return subscriptionService.cancelSubscription(principal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createSubscription")
    public SubscriptionDTO createSubscription(@RequestBody SubscriptionDTO newSubscription) {
        return subscriptionService.createSubscription(newSubscription);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteSubscription/{id}")
    public ResultDTO deleteSubscription(@PathVariable(value = "id") int id, Principal principal) {
        return subscriptionService.deleteSubscription(id, principal);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateSubscription/{id}")
    public SubscriptionDTO updateSubscription(@PathVariable(value = "id") int id, @RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.updateSubscription(id, subscriptionDTO);
    }
}
