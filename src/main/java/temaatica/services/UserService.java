package temaatica.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import temaatica.dtos.UserDTO;
import temaatica.models.*;
import temaatica.repositories.FirestoreRepository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FirestoreRepository firestoreRepository;

    public List<QueryDocumentSnapshot> getAllUsers() throws InterruptedException, ExecutionException {
        return firestoreRepository.getAllDocuments("telegramUsers");
    }

    public void updateUserBalance(String userId, double balance) throws ExecutionException, InterruptedException {
        DocumentSnapshot docSnapshot = firestoreRepository.getDocument("telegramUsers", userId);
        if (docSnapshot.exists()) {
            User user = docSnapshot.toObject(User.class);
            user.setBalance(balance);
            firestoreRepository.createOrUpdateDocument("telegramUsers", userId, user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User createOrUpdateUser(User user) throws ExecutionException, InterruptedException {
        firestoreRepository.createOrUpdateDocument("telegramUsers", user.getUserId(), user);
        return user;
    }

    public Map<String, Integer> getRemainingClicks(String userId) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestoreRepository.getDocument("telegramUsers", userId);
        if (doc.exists()) {
            User user = doc.toObject(User.class);
            Map<String, Integer> remainingClicks = new HashMap<>();
            remainingClicks.put("freeGuru", user.getFreeGuru());
            remainingClicks.put("fullTank", user.getFullTank());
            return remainingClicks;
        }
        return Collections.emptyMap();
    }

    public double getTotalBalance() throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = firestoreRepository.getAllDocuments("telegramUsers");
        double totalBalance = 0;
        for (QueryDocumentSnapshot doc : documents) {
            User user = doc.toObject(User.class);
            totalBalance += user.getBalance();
        }
        return totalBalance;
    }

    public List<Referral> getReferrals(String userId) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestoreRepository.getDocument("telegramUsers", userId);
        if (doc.exists()) {
            User user = doc.toObject(User.class);
            return user.getReferrals();
        }
        return Collections.emptyList();
    }


    public void createUser(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", user.getFirstName());
        data.put("lastName", user.getLastName());
        data.put("username", user.getUsername());
        data.put("userId", user.getUserId());
        data.put("balance", user.getBalance());
        data.put("battery", user.getBattery());
        data.put("energy", user.getEnergy());
        data.put("freeGuru", user.getFreeGuru());
        data.put("fullTank", user.getFullTank());
        data.put("level", user.getLevel());
        data.put("refBonus", user.getRefBonus());
        data.put("refereeId", user.getRefereeId());
        data.put("referrals", user.getReferrals());
        data.put("tapBalance", user.getTapBalance());
        data.put("tapValue", user.getTapValue());
        data.put("timeRefill", user.getTimeRefill());
        data.put("timeSta", user.getTimeSta());
        data.put("timeStaTank", user.getTimeStaTank());
        data.put("totalBalance", user.getTotalBalance());

        firestoreRepository.createDocument("telegramUsers", data);
    }

    public User getUser(String userId) throws InterruptedException, ExecutionException {
        DocumentSnapshot doc = firestoreRepository.getDocument("telegramUsers", userId);
        if (doc.exists()) {
            return doc.toObject(User.class);
        }
        return null;
    }

    public void updateUserStats(String userId, int newBalance, int newEnergy) throws InterruptedException, ExecutionException {
        Map<String, Object> updates = new HashMap<>();
        updates.put("balance", newBalance);
        updates.put("energy", newEnergy);
        firestoreRepository.updateDocument("telegramUsers", userId, updates);
    }

    public void updateUserStats(String userId, int newBalance, int newEnergy, int newTapBalance) throws InterruptedException, ExecutionException {
        Map<String, Object> updates = new HashMap<>();
        updates.put("balance", newBalance);
        updates.put("energy", newEnergy);
        updates.put("tapBalance", newTapBalance);
        firestoreRepository.updateDocument("telegramUsers", userId, updates);
    }

    public void updateUserData(String userId, UserDTO userDTO) throws Exception {
        DocumentSnapshot userDocument = firestoreRepository.getDocument("telegramUsers", userId);
        if (userDocument == null || !userDocument.exists()) {
            throw new Exception("User not found");
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("balance", userDTO.getBalance());
        updates.put("energy", userDTO.getEnergy());
        updates.put("tapBalance", userDTO.getTapBalance());

        firestoreRepository.updateDocument("telegramUsers", userId, updates);
    }

    public void updateUserLevel(String userId, Level level) throws Exception {
        DocumentSnapshot userDocument = firestoreRepository.getDocument("telegramUsers", userId);
        if (userDocument == null || !userDocument.exists()) {
            throw new Exception("User not found");
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("level", level);

        firestoreRepository.updateDocument("telegramUsers", userId, updates);
    }

    public void upgradeTapValue(String userId, Map<String, Object> tapValueMap, Double balance) throws ExecutionException, InterruptedException {
        DocumentSnapshot docSnapshot = firestoreRepository.getDocument("telegramUsers", userId);
        if (docSnapshot.exists()) {
            User user = docSnapshot.toObject(User.class);
            ObjectMapper mapper = new ObjectMapper();
            TapValue tapValue = mapper.convertValue(tapValueMap, TapValue.class);
            user.setTapValue(tapValue);
            user.setBalance(balance);
            firestoreRepository.createOrUpdateDocument("telegramUsers", userId, user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void upgradeBattery(String userId, Map<String, Object> batteryMap, int energy, Double balance) throws ExecutionException, InterruptedException {
        DocumentSnapshot docSnapshot = firestoreRepository.getDocument("telegramUsers", userId);
        if (docSnapshot.exists()) {
            User user = docSnapshot.toObject(User.class);
            ObjectMapper mapper = new ObjectMapper();
            Battery battery = mapper.convertValue(batteryMap, Battery.class);
            user.setBattery(battery);
            user.setEnergy(energy);
            user.setBalance(balance);
            firestoreRepository.createOrUpdateDocument("telegramUsers", userId, user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void upgradeTimeRefill(String userId, Map<String, Object> timeRefillMap, Double balance) throws ExecutionException, InterruptedException {
        DocumentSnapshot docSnapshot = firestoreRepository.getDocument("telegramUsers", userId);
        if (docSnapshot.exists()) {
            User user = docSnapshot.toObject(User.class);
            ObjectMapper mapper = new ObjectMapper();
            TimeRefill timeRefill = mapper.convertValue(timeRefillMap, TimeRefill.class);
            user.setTimeRefill(timeRefill);
            user.setBalance(balance);
            firestoreRepository.createOrUpdateDocument("telegramUsers", userId, user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void useFreeGuru(String userId, Integer freeGuru, Date timeSta) throws ExecutionException, InterruptedException {
        DocumentSnapshot docSnapshot = firestoreRepository.getDocument("telegramUsers", userId);
        if (docSnapshot.exists()) {
            User user = docSnapshot.toObject(User.class);
            user.setFreeGuru(freeGuru);
            user.setTimeSta(timeSta);
            firestoreRepository.createOrUpdateDocument("telegramUsers", userId, user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void useFullTank(String userId, Integer fullTank, Date timeStaTank) throws ExecutionException, InterruptedException {
        DocumentSnapshot docSnapshot = firestoreRepository.getDocument("telegramUsers", userId);
        if (docSnapshot.exists()) {
            User user = docSnapshot.toObject(User.class);
            user.setFullTank(fullTank);
            user.setTimeStaTank(timeStaTank);
            firestoreRepository.createOrUpdateDocument("telegramUsers", userId, user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Map<String, Object> claimReferralReward(String userId, String rewardTitle, Double bonusAward) throws ExecutionException, InterruptedException {
        DocumentSnapshot docSnapshot = firestoreRepository.getDocument("telegramUsers", userId);
        if (docSnapshot.exists()) {
            User user = docSnapshot.toObject(User.class);

            List<String> claimedReferralRewards = user.getClaimedReferralRewards();
            if (claimedReferralRewards == null) {
                claimedReferralRewards = new ArrayList<>();
            }
            if (claimedReferralRewards.contains(rewardTitle)) {
                throw new RuntimeException("Награда уже получена");
            }

            List<Referral> referrals = user.getReferrals();
            int referralsRequired = getReferralsRequiredForReward(rewardTitle);
            if (referrals == null || referrals.size() < referralsRequired) {
                throw new RuntimeException("Недостаточно рефералов для получения награды");
            }

            double newBalance = user.getBalance() + bonusAward;
            claimedReferralRewards.add(rewardTitle);

            user.setBalance(newBalance);
            user.setClaimedReferralRewards(claimedReferralRewards);
            firestoreRepository.createOrUpdateDocument("telegramUsers", userId, user);
            Map<String, Object> response = new HashMap<>();
            response.put("newBalance", newBalance);
            response.put("updatedClaimedRewards", claimedReferralRewards);

            return response;
        } else {
            throw new RuntimeException("Пользователь не найден");
        }
    }

    private int getReferralsRequiredForReward(String rewardTitle) {
        switch (rewardTitle) {
            case "Invite 3 friends":
                return 1;
            case "Invite 5 friends":
                return 2;
            case "Invite 10 friends":
                return 10;
            default:
                throw new RuntimeException("Неизвестная награда");
        }
    }

    public void deleteUser(String userId) {
        firestoreRepository.deleteDocument("telegramUsers", userId);
    }
}
