package com.norbert.tfa.authentication;

import com.norbert.tfa.authentication.request.LoginRequest;
import com.norbert.tfa.authentication.request.RegistrationRequest;
import com.norbert.tfa.authentication.request.VerificationRequest;
import com.norbert.tfa.authentication.response.AuthenticationResponse;
import com.norbert.tfa.exception.BadRegistrationRequest;
import com.norbert.tfa.jwt.JwtTokenService;
import com.norbert.tfa.user.Role;
import com.norbert.tfa.user.User;
import com.norbert.tfa.user.UserDetailsImpl;
import com.norbert.tfa.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final TwoFactorAuthenticationService tfaService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegistrationRequest request) {
        if (userService.isUserPresent(request.email()))
            throw new BadRegistrationRequest("Email is already taken, try to use another");
        final User user = buildUser(request);
        userService.save(user);
        final String jwtToken = jwtTokenService.generateToken(new UserDetailsImpl(user));
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
    private User buildUser(RegistrationRequest request){
        return User.builder()
                .tfaEnabled(false)
                .tfaSecret(null)
                .role(Role.USER)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticateByManager(request);
        User user = userService.findByEmail(request.getEmail());
        String jwtToken = null;
        if (!user.isTfaEnabled())
            jwtToken = jwtTokenService.generateToken(new UserDetailsImpl(user));
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .tfaEnabled(user.isTfaEnabled())
                .build();
    }

    private void authenticateByManager(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }

    public AuthenticationResponse verify(VerificationRequest request) {
        authenticateByManager(request);
        User user = userService.findByEmail(request.getEmail());
        if (!user.isTfaEnabled() || tfaService.isOtpNotValid(user.getTfaSecret(), request.getCode()))
            throw new BadCredentialsException("Code is not correct");
        final String jwtToken = jwtTokenService.generateToken(new UserDetailsImpl(user));
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .tfaEnabled(user.isTfaEnabled())
                .build();
    }
}
