package com.test.demo.service;

import com.test.demo.dto.ResultDTO;
import com.test.demo.dto.ReviewDTO;
import com.test.demo.model.Client;
import com.test.demo.model.Review;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ClientRepository clientRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ClientRepository clientRepository) {
        this.reviewRepository = reviewRepository;
        this.clientRepository = clientRepository;
    }

    public List<Review> getAllByClientId(int clientId) {
        return reviewRepository.findByClientId(clientId);
    }

    public ReviewDTO createReview(ReviewDTO review, Principal principal) {
        Client client = new Client();
        if(clientRepository.findByUsername(principal.getName()) == null){
            client = clientRepository.findClientByEmail(principal.getName());
        }
        else{
            client = clientRepository.findByUsername(principal.getName());
        }
        Review newReview = new Review()
                .setClient(client)
                .setDescription(review.getDescription())
                .setTitle(review.getTitle());

        return new ReviewDTO(reviewRepository.save(newReview));
    }

    public ResultDTO deleteReview(int id) {
        reviewRepository.deleteById(id);
        return new ResultDTO().setMessage("Review deleted!").setStatus(true);
    }

    public Page<ReviewDTO> getAll(int page, int size, Principal principal) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Review> pageResult = reviewRepository.findAll(pageRequest);

        List<ReviewDTO> reviews = pageResult.
                stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(reviews, pageRequest, pageResult.getTotalElements());
    }
}
