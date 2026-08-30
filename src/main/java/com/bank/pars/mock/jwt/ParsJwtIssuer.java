package com.bank.pars.mock.jwt;

import com.bank.pars.mock.config.ParsMockProperties;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

@Service
public class ParsJwtIssuer {

    private static final Set<String> ALLOWED_SCOPES = Set.of(
            "tokens:read",
            "certificates:read",
            "verification:execute",
            "system:read");

    private final JwtEncoder encoder;
    private final ParsMockProperties properties;

    public ParsJwtIssuer(JwtEncoder encoder, ParsMockProperties properties) {
        this.encoder = encoder;
        this.properties = properties;
    }

    public TokenResponse issueAccessToken(AccessTokenRequest request) {
        List<String> scopes = request.scopes() == null || request.scopes().isEmpty()
                ? List.copyOf(ALLOWED_SCOPES)
                : request.scopes().stream()
                        .filter(ALLOWED_SCOPES::contains)
                        .distinct()
                        .toList();

        Duration lifetime = properties.accessTokenLifetime();
        Instant now = Instant.now();
        JwtClaimsSet claims = baseClaims(subject(request.subject()), now, lifetime)
                .claim("token_use", "access")
                .claim("scope", String.join(" ", scopes))
                .build();
        return encode(claims, lifetime);
    }

    public TokenResponse issueSigningToken(SigningTokenRequest request) {
        Duration lifetime = properties.signingTokenLifetime();
        Instant now = Instant.now();
        JwtClaimsSet claims = baseClaims(subject(request.subject()), now, lifetime)
                .id(UUID.randomUUID().toString())
                .claim("token_use", "signing")
                .claim("operation", request.operation().name())
                .claim("documentDigest", request.documentDigest())
                .claim("checkExpire", request.checkExpire())
                .build();
        return encode(claims, lifetime);
    }

    private JwtClaimsSet.Builder baseClaims(
            String subject,
            Instant now,
            Duration lifetime) {
        return JwtClaimsSet.builder()
                .issuer(properties.issuer())
                .audience(List.of(properties.audience()))
                .subject(subject)
                .issuedAt(now)
                .expiresAt(now.plus(lifetime));
    }

    private TokenResponse encode(JwtClaimsSet claims, Duration lifetime) {
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        String value = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new TokenResponse(value, "Bearer", lifetime.toSeconds());
    }

    private static String subject(String subject) {
        return subject == null || subject.isBlank() ? "mock-pars-user" : subject;
    }
}
