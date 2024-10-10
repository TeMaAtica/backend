package temaatica.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import temaatica.services.UserService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TotalBalanceController {

    private final UserService userService;

    @GetMapping("/totalBalance")
    public ResponseEntity<Map<String, Object>> getTotalBalance() {
        try {
            double totalBalance = userService.getTotalBalance();
            Map<String, Object> response = new HashMap<>();
            response.put("totalBalance", totalBalance);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateBalance")
    public ResponseEntity<?> updateBalance(@RequestBody Map<String, Object> payload) {
        try {
            String userId = (String) payload.get("userId");
            Number balanceNumber = (Number) payload.get("balance");
            if (userId == null || balanceNumber == null) {
                return ResponseEntity.badRequest().body("userId and balance are required");
            }
            double balance = balanceNumber.doubleValue();
            userService.updateUserBalance(userId, balance);
            return ResponseEntity.ok("Balance updated successfully");
        } catch (Exception e) {
            log.error("Error updating balance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating balance");
        }
    }
}

