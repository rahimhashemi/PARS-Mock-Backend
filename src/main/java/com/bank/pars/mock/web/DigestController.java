package com.bank.pars.mock.web;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mock/digest")
public class DigestController {

    @PostMapping("/text")
    public Map<String, String> text(@RequestBody String document) throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(document.getBytes(StandardCharsets.UTF_8));
        return Map.of(
                "algorithm", "SHA-256",
                "documentDigest",
                Base64.getUrlEncoder().withoutPadding().encodeToString(digest));
    }
}
