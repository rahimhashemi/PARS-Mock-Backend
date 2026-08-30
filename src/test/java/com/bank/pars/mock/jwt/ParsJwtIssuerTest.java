package com.bank.pars.mock.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.bank.pars.mock.config.JwtConfiguration;
import com.bank.pars.mock.config.ParsMockProperties;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

class ParsJwtIssuerTest {

    @Test
    void issuesAccessAndSigningTokensWithTokenBridgeClaims() throws Exception {
        JwtConfiguration configuration = new JwtConfiguration();
        KeyPair pair = configuration.parsMockKeyPair();
        RSAKey rsaKey = configuration.parsMockRsaKey(pair);
        var encoder = configuration.jwtEncoder(rsaKey);
        var properties = new ParsMockProperties(
                "http://127.0.0.1:9080",
                "token-bridge",
                Duration.ofMinutes(5),
                Duration.ofMinutes(2));
        ParsJwtIssuer issuer = new ParsJwtIssuer(encoder, properties);

        JwtDecoder decoder = NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();

        TokenResponse access = issuer.issueAccessToken(
                new AccessTokenRequest("u1", List.of("tokens:read")));
        Jwt accessJwt = decoder.decode(access.accessToken());
        assertThat(accessJwt.getClaimAsString("token_use")).isEqualTo("access");
        assertThat(accessJwt.getClaimAsString("scope")).isEqualTo("tokens:read");

        TokenResponse signing = issuer.issueSigningToken(
                new SigningTokenRequest("u1", SigningOperation.SIGN_CMS, "digest", true));
        Jwt signingJwt = decoder.decode(signing.accessToken());
        assertThat(signingJwt.getClaimAsString("token_use")).isEqualTo("signing");
        assertThat(signingJwt.getClaimAsString("operation")).isEqualTo("SIGN_CMS");
        assertThat(signingJwt.getClaimAsString("documentDigest")).isEqualTo("digest");
        assertThat(signingJwt.getClaimAsBoolean("checkExpire")).isTrue();
        assertThat(signingJwt.getId()).isNotBlank();
    }
}
