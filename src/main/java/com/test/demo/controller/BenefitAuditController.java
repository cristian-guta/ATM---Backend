package com.test.demo.controller;

import com.test.demo.model.BenefitAudit;
import com.test.demo.model.RevisionInfo;
import com.test.demo.repository.BenefitAuditRepository;
import com.test.demo.repository.RevisionInfoRepository;
import com.test.demo.service.BenefitService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RestController
@RequestMapping("/api/audit/benefit")
public class BenefitAuditController {

    private BenefitService benefitService;
    private BenefitAuditRepository benefitAuditRepository;
    private RevisionInfoRepository revisionInfoRepository;

    @Autowired
    public BenefitAuditController(BenefitService benefitService, BenefitAuditRepository benefitAuditRepository, RevisionInfoRepository revisionInfoRepository) {
        this.benefitAuditRepository = benefitAuditRepository;
        this.benefitService = benefitService;
        this.revisionInfoRepository = revisionInfoRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAuditInfo/{page}/{size}")
    public Page<BenefitAudit> getAuditInfo(@PathVariable(value = "page") int page,
                                           @PathVariable(value = "size") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        benefitAuditRepository.findAll().forEach(benefitAudit -> {
            RevisionInfo revisionInfo = revisionInfoRepository.findById(benefitAudit.getRev());
            benefitAudit.setUser(revisionInfo.getUser());
            benefitAuditRepository.save(benefitAudit);
        });
        Page<BenefitAudit> pageResult = benefitAuditRepository.findAll(pageRequest);

        List<BenefitAudit> benefitAudits = pageResult
                .stream()
//                .map(BenefitAudit::new)
                .collect(Collectors.toList());

        return new PageImpl<>(benefitAudits, pageRequest, pageResult.getTotalElements());
    }

}
