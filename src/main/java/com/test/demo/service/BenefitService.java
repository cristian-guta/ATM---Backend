package com.test.demo.service;

import com.test.demo.dto.BenefitDTO;
import com.test.demo.model.Benefit;
import com.test.demo.repository.BenefitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BenefitService {

    private BenefitRepository benefitRepository;
    private Logger log = Logger.getLogger(BenefitService.class.getName());

    @Autowired
    public BenefitService(BenefitRepository benefitRepository) {
        this.benefitRepository = benefitRepository;
    }

    public void seedBenefits() {
        seedBenefit(1, "Minute nationale");
        seedBenefit(2, "SMS");
        seedBenefit(3, "Apeluri video");
        seedBenefit(4, "Minute internationale");
        seedBenefit(5, "Internet nelimitat");
        seedBenefit(6, "Roaming");
    }

    private void seedBenefit(int id, String description) {
        Benefit benefit = benefitRepository.getById(id);
        if (benefit == null) {
            benefit = new Benefit().setId(id).setDescription(description);
            benefitRepository.save(benefit);
        }
    }

    public List<BenefitDTO> getAllBenefits(){
                List<BenefitDTO> benefits = new ArrayList<>();
        for (Benefit ben : benefitRepository.findAll()) {
            BenefitDTO bnf = new BenefitDTO()
                    .setId(ben.getId())
                    .setDescription(ben.getDescription());
            benefits.add(bnf);
        }
        return benefits;
    }

    public Page<BenefitDTO> getAllBenefitsPaged(int page, int size) {

        log.info("Listing ALL benefits...");
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Benefit> pageResult = benefitRepository.findAll(pageRequest);
        List<BenefitDTO> benefits = pageResult
                .stream()
                .map(BenefitDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(benefits, pageRequest, pageResult.getTotalElements());
    }

    public List<BenefitDTO> getBenefitsBySubscription(Principal principal, int id) {

        log.info("Listing all benefits by subscription...");

        List<BenefitDTO> benefits = new ArrayList<>();
        benefitRepository.findBySubscriptionId(id).forEach(benefit -> {
            BenefitDTO ben = new BenefitDTO().setId(benefit.getId())
                    .setDescription(benefit.getDescription());

            benefits.add(ben);
        });
        return benefits.stream().distinct().collect(Collectors.toList());
    }
}
