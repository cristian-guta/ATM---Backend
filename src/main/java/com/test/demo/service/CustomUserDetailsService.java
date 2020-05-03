package com.test.demo.service;

import com.test.demo.dto.ClientDTO;
import com.test.demo.model.Client;
import com.test.demo.model.JwtRequest;
import com.test.demo.model.JwtResponse;
import com.test.demo.model.Role;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.RoleRepository;
import com.test.demo.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private Logger log = Logger.getLogger(CustomUserDetailsService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws ResponseStatusException {
        log.info("Loading user by username...");
        Client client = clientRepository.findByUsername(username);
        if (client == null) {
            log.info("User not found...");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        } else if (client.getStatus()) {
            log.info("Invalid credentials...");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials!");
        }
        log.info("Loading...");
        return new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(),
                getAuthority(client));
    }

    private List<SimpleGrantedAuthority> getAuthority(Client client) {
        log.info("Fetching authority...");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Role role = client.getRole();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

    public Client save(ClientDTO user) {
        log.info("Saving new client account...");
        Client newUser = new Client()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setPassword(bcryptEncoder.encode(user.getPassword()))
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setCnp(user.getCnp())
                .setAddress(user.getAddress());
        Role role = roleRepository.findByName("USER");
        newUser.setRole(role);
        log.info("New client saved...");
        return clientRepository.save(newUser);
    }

    public JwtResponse handleLogin(JwtRequest authenticationRequest) throws Exception {
        log.info("Authenticating process...");

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            log.info("User disabled...");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            log.info("Invalid credentials...");
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
