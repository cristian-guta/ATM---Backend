package com.test.demo.dto;

import com.test.demo.model.Client;
import com.test.demo.model.Review;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReviewDTO {
    private int id;
    private String title;
    private String description;
    private int userId;
    private Client client;

    public ReviewDTO() {
    }

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.description = review.getDescription();
        this.client = review.getClient();
    }
}
