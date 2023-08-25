package com.norbert.tfa.authentication;

import com.norbert.tfa.authentication.request.LoginRequest;
import com.norbert.tfa.authentication.request.RegistrationRequest;
import com.norbert.tfa.authentication.request.TFAConfirmationRequest;
import com.norbert.tfa.authentication.request.VerificationRequest;
import com.norbert.tfa.authentication.response.AuthenticationResponse;
import com.norbert.tfa.authentication.response.TFAEnablingResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;
    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/2fa/confirm-enabling")
    public ResponseEntity<Void> confirmEnablingTFA(
            @RequestBody TFAConfirmationRequest tfaConfirmationRequest
    ){
        twoFactorAuthenticationService.confirmEnablingTFA(tfaConfirmationRequest);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/2fa/enable")
    public ResponseEntity<TFAEnablingResponse> enable(){
        return ResponseEntity.ok(twoFactorAuthenticationService.enable());
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<AuthenticationResponse> verify(
            @RequestBody VerificationRequest request
    ){
        return ResponseEntity.ok(authenticationService.verify(request));
    }
}
