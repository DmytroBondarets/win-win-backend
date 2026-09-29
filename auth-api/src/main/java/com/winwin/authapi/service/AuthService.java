package com.winwin.authapi.service;

import com.winwin.authapi.dto.AuthRequest;
import com.winwin.authapi.entity.ProcessingLog;
import com.winwin.authapi.entity.User;
import com.winwin.authapi.repository.ProcessingLogRepository;
import com.winwin.authapi.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ProcessingLogRepository processingLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    @Value("${data.api.url}")
    private String dataApiUrl;

    @Value("${internal.token}")
    private String internalToken;

    public AuthService(UserRepository userRepository,
                       ProcessingLogRepository processingLogRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.processingLogRepository = processingLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
    }

    public void register(AuthRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User(request.email(), passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    public String login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return jwtService.generateToken(user);
    }

    public String processText(User user, String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", internalToken);

        Map<String, String> payload = Map.of("text", text);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                dataApiUrl + "/api/transform",
                HttpMethod.POST,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("result")) {
            throw new IllegalStateException("Transformation service returned an invalid response");
        }

        String result = String.valueOf(body.get("result"));
        processingLogRepository.save(new ProcessingLog(user, text, result, LocalDateTime.now()));
        return result;
    }
}
