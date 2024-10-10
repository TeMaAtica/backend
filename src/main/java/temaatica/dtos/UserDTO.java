package temaatica.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class UserDTO {
    private double balance;
    private double energy;
    private double tapBalance;
}