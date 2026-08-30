package com.bank.pars.mock.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pars.mock")
public record ParsMockProperties(
        String issuer,
        String audience,
        Duration accessTokenLifetime,
        Duration signingTokenLifetime) {
}
