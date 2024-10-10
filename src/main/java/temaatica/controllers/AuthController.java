package temaatica.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import temaatica.services.TelegramAuthService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class AuthController {

    @Value("${secret.key}")
    private String secretKey;

    private final TelegramAuthService telegramAuthService;

    public AuthController(TelegramAuthService telegramAuthService) {
        this.telegramAuthService = telegramAuthService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> payload) {
        String initData = payload.get("initData");
        log.info(initData);
        if (telegramAuthService.validateInitData(initData)) {
            String userId = telegramAuthService.extractUserIdFromInitData(initData);
            String token = generateJwtToken(userId);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String generateJwtToken(String userId) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(userId)
                .signWith(key)
                .compact();
    }
}

