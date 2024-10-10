package temaatica.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import temaatica.dtos.UserDTO;
import temaatica.models.Level;
import temaatica.models.Referral;
import temaatica.models.User;
import temaatica.services.UserService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers().stream()
                    .map(doc -> doc.toObject(User.class))
                    .collect(Collectors.toList());
            log.error(users.toString());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("An error occurred while fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createOrUpdateUser(@RequestBody User user) {
        try {
            User savedUser = userService.createOrUpdateUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        try {
            User user = userService.getUser(userId);
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/upgradeTapValue")
    public ResponseEntity<?> upgradeTapValue(@RequestBody Map<String, Object> payload) {
        try {
            String userId = (String) payload.get("userId");
            Map<String, Object> tapValue = (Map<String, Object>) payload.get("tapValue");
            Double balance = ((Number) payload.get("balance")).doubleValue();

            userService.upgradeTapValue(userId, tapValue, balance);
            return ResponseEntity.ok("Tap value upgraded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while upgrading tap value");
        }
    }

    @PutMapping("/upgradeBattery")
    public ResponseEntity<?> upgradeBattery(@RequestBody Map<String, Object> payload) {
        try {
            String userId = (String) payload.get("userId");
            Map<String, Object> battery = (Map<String, Object>) payload.get("battery");
            int energy = ((Number) payload.get("energy")).intValue();
            Double balance = ((Number) payload.get("balance")).doubleValue();

            userService.upgradeBattery(userId, battery, energy, balance);
            return ResponseEntity.ok("Battery upgraded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while upgrading battery");
        }
    }

    @PutMapping("/upgradeTimeRefill")
    public ResponseEntity<?> upgradeTimeRefill(@RequestBody Map<String, Object> payload) {
        try {
            String userId = (String) payload.get("userId");
            Map<String, Object> timeRefill = (Map<String, Object>) payload.get("timeRefill");
            Double balance = ((Number) payload.get("balance")).doubleValue();

            userService.upgradeTimeRefill(userId, timeRefill, balance);
            return ResponseEntity.ok("Time refill upgraded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while upgrading time refill");
        }
    }

    @PutMapping("/useFreeGuru")
    public ResponseEntity<?> useFreeGuru(@RequestBody Map<String, Object> payload) {
        try {
            String userId = (String) payload.get("userId");
            Integer freeGuru = (Integer) payload.get("freeGuru");
            Date timeSta = new Date();

            userService.useFreeGuru(userId, freeGuru, timeSta);
            return ResponseEntity.ok("FreeGuru used successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while using FreeGuru");
        }
    }

    @PutMapping("/useFullTank")
    public ResponseEntity<?> useFullTank(@RequestBody Map<String, Object> payload) {
        try {
            String userId = (String) payload.get("userId");
            Integer fullTank = (Integer) payload.get("fullTank");
            Date timeStaTank = new Date();

            userService.useFullTank(userId, fullTank, timeStaTank);
            return ResponseEntity.ok("FullTank used successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while using FullTank");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserStats(@PathVariable String userId, @RequestParam int balance, @RequestParam int energy) {
        try {
            userService.updateUserStats(userId, balance, energy);
            return ResponseEntity.noContent().build();
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{userId}/level")
    public ResponseEntity<Void> updateUserLevel(@PathVariable String userId, @RequestBody Map<String, Object> payload) {
        try {
            Level level = new ObjectMapper().convertValue(payload.get("level"), Level.class);
            userService.updateUserLevel(userId, level);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/claimReferralReward")
    public ResponseEntity<?> claimReferralReward(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            String rewardTitle = (String) payload.get("rewardTitle");
            Double bonusAward = ((Number) payload.get("bonusAward")).doubleValue();

            // Получаем информацию о пользователе из токена
            String userId = authentication.getName();

            Map<String, Object> response = userService.claimReferralReward(userId, rewardTitle, bonusAward);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при получении реферальной награды", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при получении реферальной награды");
        }
    }

    @GetMapping("/data")
    public ResponseEntity<User> getUserData(Authentication authentication) {
        try {
            String userId = authentication.getName();
            User user = userService.getUser(userId);
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUserData(Authentication authentication, @RequestBody UserDTO userDTO) {
        try {
            String userId = authentication.getName();
            userService.updateUserData(userId, userDTO);
            return ResponseEntity.ok("User data updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user data");
        }
    }

    @GetMapping("/referrals")
    public ResponseEntity<List<Referral>> getReferrals(Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<Referral> referrals = userService.getReferrals(userId);
            return ResponseEntity.ok(referrals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/remainingClicks")
    public ResponseEntity<Map<String, Integer>> getRemainingClicks(@PathVariable String userId) {
        try {
            Map<String, Integer> remainingClicks = userService.getRemainingClicks(userId);
            return ResponseEntity.ok(remainingClicks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/{userId}/tapBalance")
//    public ResponseEntity<Void> updateUserStats(
//            @PathVariable String userId,
//            @RequestParam int balance,
//            @RequestParam int energy,
//            @RequestParam int tapBalance) {
//        try {
//            userService.updateUserStats(userId, balance, energy, tapBalance);
//            return ResponseEntity.noContent().build();
//        } catch (InterruptedException | ExecutionException e) {
//            return ResponseEntity.status(500).build();
//        }
//    }

}
