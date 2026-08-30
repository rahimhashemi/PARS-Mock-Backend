package com.bank.pars.mock.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ParsMockProperties.class)
public class JwtConfiguration {

    @Bean
    public KeyPair parsMockKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(3072);
        return generator.generateKeyPair();
    }

    @Bean
    public RSAKey parsMockRsaKey(KeyPair parsMockKeyPair) {
        return new RSAKey.Builder((RSAPublicKey) parsMockKeyPair.getPublic())
                .privateKey((RSAPrivateKey) parsMockKeyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAKey parsMockRsaKey) {
        return new NimbusJwtEncoder(
                new ImmutableJWKSet<>(new JWKSet(parsMockRsaKey)));
    }
}
