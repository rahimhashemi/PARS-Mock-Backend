package com.bank.pars.mock.web;

import com.bank.pars.mock.jwt.AccessTokenRequest;
import com.bank.pars.mock.jwt.ParsJwtIssuer;
import com.bank.pars.mock.jwt.SigningTokenRequest;
import com.bank.pars.mock.jwt.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mock/jwt")
public class JwtController {

    private final ParsJwtIssuer issuer;

    public JwtController(ParsJwtIssuer issuer) {
        this.issuer = issuer;
    }

    @PostMapping("/access")
    public TokenResponse access(@RequestBody(required = false) AccessTokenRequest request) {
        return issuer.issueAccessToken(
                request == null ? new AccessTokenRequest(null, null) : request);
    }

    @PostMapping("/signing")
    public TokenResponse signing(@Valid @RequestBody SigningTokenRequest request) {
        return issuer.issueSigningToken(request);
    }
}
