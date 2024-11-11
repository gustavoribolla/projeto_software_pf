package br.insper.pf.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TokenValidator {

    @Value("${token.validation.url:184.72.80.215}")
    private String tokenValidationUrl;

    public boolean validateToken(String token, String... allowedRoles) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        token = token.substring(7);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(tokenValidationUrl, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = response.getBody();
            String role = (String) responseBody.get("role");
            for (String allowedRole : allowedRoles) {
                if (allowedRole.equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }
}