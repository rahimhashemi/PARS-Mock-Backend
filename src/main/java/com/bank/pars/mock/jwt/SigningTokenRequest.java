package com.bank.pars.mock.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SigningTokenRequest(
        String subject,
        @NotNull SigningOperation operation,
        @NotBlank String documentDigest,
        boolean checkExpire) {
}
