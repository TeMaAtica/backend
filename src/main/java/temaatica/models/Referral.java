package temaatica.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class Referral {
    private int balance;
    private int balanceDifference;
    private Level level;
    private String userId;
    private String username;
}