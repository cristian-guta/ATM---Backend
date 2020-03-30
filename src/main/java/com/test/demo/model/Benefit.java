package com.test.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "benefit")
@AllArgsConstructor
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String description;

//    @ManyToMany(mappedBy = "benefits", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//    private List<Subscription> subscriptions;

    public Benefit() {
    }
}
