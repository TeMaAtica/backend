package temaatica.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class Battery {
    private int level;
    private int energy;
}
