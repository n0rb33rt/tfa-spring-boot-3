package com.norbert.tfa.authentication;

import com.norbert.tfa.authentication.request.TFAConfirmationRequest;
import com.norbert.tfa.authentication.response.TFAEnablingResponse;
import com.norbert.tfa.exception.TFAException;
import com.norbert.tfa.user.User;
import com.norbert.tfa.user.UserDetailsImpl;
import com.norbert.tfa.user.UserService;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class TwoFactorAuthenticationService {
    private final UserService userService;
    public String generateSecret() {
        return new DefaultSecretGenerator().generate();
    }

    public String generateQrCodeImageUri(String secret) {
        QrData data = new QrData.Builder()
                .label("Two-factor Authentication Example")
                .issuer("Norbert's application")
                .secret(secret)
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();
        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];
        try {
            imageData = qrGenerator.generate(data);
        }catch (QrGenerationException e){
            log.info("Error while generating a QR-code");
        }
        return Utils.getDataUriForImage(imageData,qrGenerator.getImageMimeType());
    }
    public boolean isOtpValid(String secret,String code){
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator,timeProvider);
        return verifier.isValidCode(secret, code);
    }
    public boolean isOtpNotValid(String secret,String code){
        return !this.isOtpValid(secret,code);
    }

    public void confirmEnablingTFA(
            TFAConfirmationRequest request
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        if (isOtpNotValid(request.secretKey(), request.code()))
            throw new BadCredentialsException("Code is not correct");
        user.setTfaEnabled(true);
        user.setTfaSecret(request.secretKey());
        userService.save(user);
    }
    public TFAEnablingResponse enable(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        if(user.isTfaEnabled())
            throw new TFAException("Two-factor authentication is already on");
        final String tfaSecretKey = generateSecret();
        final String tfaSecretImageUri = generateQrCodeImageUri(tfaSecretKey);
        return TFAEnablingResponse.builder()
                .secretImageUri(tfaSecretImageUri)
                .secretKey(tfaSecretKey)
                .build();
    }

}

