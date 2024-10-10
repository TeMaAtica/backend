package temaatica.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Date;
import java.util.Map;

@ToString
@Data
@NoArgsConstructor
public class User {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private Object totalBalance;
    private double balance;
    private int tapBalance;
    private Battery battery;
    private int energy;
    private int freeGuru;
    private int fullTank;
    private Level level;
    private int refBonus;
    private String refereeId;
    private List<Referral> referrals;
    private TapValue tapValue;
    private TimeRefill timeRefill;
    private Date timeSta;
    private Date timeStaTank;
    private List<String> claimedReferralRewards;

    public String getTotalBalance() {
        if (totalBalance instanceof String) {
            return (String) totalBalance;
        } else if (totalBalance instanceof Number) {
            return String.valueOf(totalBalance);
        } else {
            return "0";
        }
    }

    public void setTotalBalance(Object totalBalance) {
        this.totalBalance = totalBalance;
    }

}