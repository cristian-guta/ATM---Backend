package com.test.demo.service;

import com.test.demo.dto.BenefitDTO;
import com.test.demo.dto.SubscriptionDTO;
import com.test.demo.model.Benefit;
import com.test.demo.model.Client;
import com.test.demo.model.Subscription;
import com.test.demo.repository.BenefitRepository;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BenefitService {

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public void seedBenefits(){
        seedBenefit(1, "Minute nationale");
        seedBenefit(2, "SMS");
        seedBenefit(3, "Apeluri video");
        seedBenefit(4, "Minute internationale");
        seedBenefit(5, "Internet nelimitat");
        seedBenefit(6, "Roaming");
    }

    private void seedBenefit(int id, String description){
        Benefit benefit = benefitRepository.getById(id);
        if(benefit == null){
            benefit = new Benefit().setId(id).setDescription(description);
            benefitRepository.save(benefit);
        }
    }

    public List<BenefitDTO> getAllBenefits() {
        List<BenefitDTO> benefits = new ArrayList<>();
        for(Benefit ben : benefitRepository.findAll()){
            BenefitDTO bnf = new BenefitDTO()
                                    .setId(ben.getId())
                                    .setDescription(ben.getDescription());
            benefits.add(bnf);
        }
        return benefits;
    }

    public List<BenefitDTO> getBenefitsBySubscription(Principal principal, int id) {
        List<BenefitDTO> benefits = new ArrayList<>();
        benefitRepository.findBySubscriptionId(id).forEach(benefit -> {
            BenefitDTO ben = new BenefitDTO().setId(benefit.getId())
                                            .setDescription(benefit.getDescription());

            benefits.add(ben);
        });
        return benefits.stream().distinct().collect(Collectors.toList());
    }
}
