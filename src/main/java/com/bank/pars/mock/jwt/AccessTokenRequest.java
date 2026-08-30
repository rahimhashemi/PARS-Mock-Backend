package com.bank.pars.mock.jwt;

import java.util.List;

public record AccessTokenRequest(String subject, List<String> scopes) {
}
