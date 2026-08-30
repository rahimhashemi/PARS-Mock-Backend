package com.bank.pars.mock.web;

import com.bank.pars.mock.jwt.Util;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mock/digest")
public class DigestController {

    @PostMapping("/text")
    public Map<String, String> text(@RequestBody String document) {
        return Map.of(
                "algorithm", "SHA-256",
                "documentDigest",
                Util.getDocumentDigest(document));
    }
}
