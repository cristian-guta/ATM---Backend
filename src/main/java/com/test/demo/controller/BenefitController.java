package com.test.demo.controller;

import com.test.demo.dto.BenefitDTO;
import com.test.demo.dto.ResultDTO;
import com.test.demo.service.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/benefits")
public class BenefitController {

    @Autowired
    private BenefitService benefitService;

    @GetMapping("/{page}/{size}")
    public Page<BenefitDTO> getAllBenefitsPaged(@PathVariable(value = "page") int page,
                                                @PathVariable(value = "size") int size) {
        return benefitService.getAllBenefitsPaged(page, size);
    }

    @GetMapping("/unpagedBenefits")
    public List<BenefitDTO> getAllBenefits() {
        return benefitService.getAllBenefits();
    }

    @GetMapping("/bySubscription/{id}")
    public List<BenefitDTO> getBenefitsBySubscription(Principal principal, @PathVariable(value = "id") int id) {
        return benefitService.getBenefitsBySubscription(principal, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createBenefit")
    public BenefitDTO createBenefit(@RequestBody BenefitDTO benefitDTO) {
        return benefitService.createBenefit(benefitDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteBenefit/{id}")
    public ResultDTO deleteBenefit(@PathVariable(value = "id") int id) {
        return benefitService.deleteBenefit(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateBenefit/{id}")
    public BenefitDTO updateBenefin(@PathVariable("id") int benefitId, @RequestBody BenefitDTO benefitDTO) {
        return benefitService.updateBenefit(benefitId, benefitDTO);
    }

}
