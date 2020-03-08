package com.test.demo.controller;


import com.test.demo.dto.ClientDTO;
import com.test.demo.model.JwtRequest;
import com.test.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAnonymous()")
@RequestMapping("/api/auth")
public class AuthenticationController {

    private CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthenticationController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ClientDTO client) {
        return ResponseEntity.ok(userDetailsService.save(client));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(userDetailsService.handleLogin(authenticationRequest));
    }
}