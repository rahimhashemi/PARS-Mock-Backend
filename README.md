# PARS Mock Backend

Development-only Spring Boot JWT issuer for testing Token Bridge without access to the real PARS backend.

## Important

- Never deploy this module as the production PARS issuer.
- The RSA private key is generated in memory on every startup.
- Token Bridge still runs with its production-style profile and verifies only the mock public key.
- The mock does not receive PINs and does not perform PKCS#11 signing.

## Run

```bash
cd tools/pars-mock-backend
mvn spring-boot:run
```

Get the public key:

```bash
curl http://127.0.0.1:9080/api/mock/public-key
```

Configure Token Bridge with the returned `publicKeyBase64`:

```text
TOKEN_BRIDGE_SECURITY_ENABLED=true
TOKEN_BRIDGE_JWT_ISSUER=http://127.0.0.1:9080
TOKEN_BRIDGE_JWT_AUDIENCE=token-bridge
TOKEN_BRIDGE_PARS_PUBLIC_KEY_BASE64=<publicKeyBase64>
```

Issue an access token:

```bash
curl -X POST http://127.0.0.1:9080/api/mock/jwt/access \
  -H "Content-Type: application/json" \
  -d '{"subject":"integration-user","scopes":["tokens:read","certificates:read"]}'
```

Issue a signing token after computing the exact Token Bridge SHA-256 Base64URL digest:

```bash
curl -X POST http://127.0.0.1:9080/api/mock/jwt/signing \
  -H "Content-Type: application/json" \
  -d '{
    "subject":"integration-user",
    "operation":"SIGN_CMS",
    "documentDigest":"<BASE64URL_SHA256>",
    "checkExpire":true
  }'
```
