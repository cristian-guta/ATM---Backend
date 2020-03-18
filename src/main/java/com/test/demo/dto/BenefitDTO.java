package com.test.demo.dto;

import com.test.demo.model.Benefit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BenefitDTO {
    private int id;
    private String description;

    public BenefitDTO(Benefit benefit) {
        this.id = benefit.getId();
        this.description = benefit.getDescription();
    }
}
