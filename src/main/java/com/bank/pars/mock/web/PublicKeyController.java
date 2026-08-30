package com.bank.pars.mock.web;

import com.nimbusds.jose.jwk.RSAKey;
import java.util.Base64;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mock")
public class PublicKeyController {

    private final RSAKey rsaKey;

    public PublicKeyController(RSAKey rsaKey) {
        this.rsaKey = rsaKey;
    }

    @GetMapping("/public-key")
    public Map<String, String> publicKey() throws Exception {
        String der = Base64.getEncoder().encodeToString(
                rsaKey.toRSAPublicKey().getEncoded());
        return Map.of(
                "algorithm", "RSA",
                "keyId", rsaKey.getKeyID(),
                "publicKeyBase64", der);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
