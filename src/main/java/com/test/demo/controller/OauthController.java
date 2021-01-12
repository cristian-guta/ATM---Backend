package com.test.demo.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.test.demo.dto.TokenDTO;
import com.test.demo.model.AuthProvider;
import com.test.demo.model.Client;
import com.test.demo.model.Role;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.RoleRepository;
import com.test.demo.security.JwtTokenUtil;
import com.test.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api/oauth")
@CrossOrigin
public class OauthController {

    @Value("${spring:\n" +
            "  security:\n" +
            "    oauth2:\n" +
            "      client:\n" +
            "        registration:\n" +
            "          google:\n" +
            "            client-id}")
    String googleClientId;

    @Value("${secretPsw}")
    String secretPsw;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;


    @PostMapping("/google")
    public ResponseEntity<TokenDTO> google(@RequestBody TokenDTO tokenDto) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                        .setAudience(Collections.singletonList(googleClientId));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
        Client client = new Client();
        if (clientRepository.findClientByEmail(payload.getEmail()) == null) {
            client = saveClient(payload.getEmail());

        } else
            client = clientRepository.findClientByEmail(payload.getEmail());
        TokenDTO tokenRes = login(client);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }


    private TokenDTO login(Client client) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(client.getEmail(), secretPsw)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(client.getEmail());
        String jwt = jwtTokenUtil.generateToken(userDetails);
        TokenDTO tokenDto = new TokenDTO();
        tokenDto.setValue(jwt);
        return tokenDto;
    }

    private Client saveClient(String email) {
        Client client = new Client();
        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(secretPsw));
        client.setAuthProvider(AuthProvider.google);
        Role userRole = roleRepository.findByName("USER");
        client.setRole(userRole);
        client.setAuthProvider(AuthProvider.google);
        return clientRepository.save(client);
    }
}
