package com.bank.pars.mock.jwt;

public record TokenResponse(String accessToken, String tokenType, long expiresInSeconds) {
}
