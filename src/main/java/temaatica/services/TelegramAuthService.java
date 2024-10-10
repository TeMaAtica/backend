package temaatica.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TelegramAuthService {


    @Value("${telegram.bot.token}")
    private String botToken;

    public boolean validateInitData(String initData) {
        try {
            String[] paramsArray = initData.split("&");
            Map<String, String> paramsMap = new TreeMap<>();
            String receivedHash = null;

            for (String param : paramsArray) {
                String[] keyValue = param.split("=");
                if (keyValue.length != 2) continue;

                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                if ("hash".equals(key)) {
                    receivedHash = value;
                } else {
                    paramsMap.put(key, value);
                }
            }

            if (receivedHash == null) return false;

            String dataCheckString = paramsMap.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("\n"));

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec("WebAppData".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(keySpec);
            byte[] secretKey = sha256Hmac.doFinal(botToken.getBytes(StandardCharsets.UTF_8));

            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hashBytes = mac.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));

            String calculatedHash = bytesToHex(hashBytes);

            return calculatedHash.equalsIgnoreCase(receivedHash);
        } catch (Exception e) {
            log.error("Ошибка при валидации initData: ", e);
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String extractUserIdFromInitData(String initData) {
        try {
            String[] paramsArray = initData.split("&");
            for (String param : paramsArray) {
                String[] keyValue = param.split("=");
                if (keyValue.length != 2) continue;

                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                if ("user".equals(key)) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> userMap = mapper.readValue(value, Map.class);
                    return String.valueOf(userMap.get("id"));
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при извлечении userId из initData: ", e);
        }
        return null;
    }
}