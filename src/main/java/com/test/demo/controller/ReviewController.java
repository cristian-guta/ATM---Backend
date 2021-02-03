package com.test.demo.controller;

import com.test.demo.dto.ResultDTO;
import com.test.demo.dto.ReviewDTO;
import com.test.demo.model.Review;
import com.test.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/{page}/{size}")
    public Page<ReviewDTO> getAll(@PathVariable(value = "page") int page,
                                        @PathVariable(value = "size") int size,
                                        Principal principal) {
        return reviewService.getAll(page, size, principal);
    }

    @GetMapping("/{id}")
    public List<Review> getAllByUserId(@PathVariable(value = "id") int clientId) {
        return reviewService.getAllByClientId(clientId);
    }

    @PostMapping("/create")
    public ReviewDTO create(@RequestBody ReviewDTO review, Principal principal) {
        return reviewService.createReview(review, principal);
    }

    @DeleteMapping("/delete/{id}")
    public ResultDTO deleteReview(@PathVariable(value = "id") int id) {
        return reviewService.deleteReview(id);
    }

}
