package com.inventory.management.auth;

import com.inventory.management.config.JwtService;
import com.inventory.management.user.UserEntity;
import com.inventory.management.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest userRequest) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.phone(), userRequest.password()));
        UserEntity user = this.userRepository.findUserByPhone(userRequest.phone()).orElseThrow();
        String jwtToken = this.jwtService.generateToken(user);
        return new LoginResponse(jwtToken);
    }

}
