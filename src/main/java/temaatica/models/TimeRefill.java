package temaatica.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class TimeRefill {
    private int duration;
    private int level;
    private int step;
}