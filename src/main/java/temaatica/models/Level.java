package temaatica.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class Level {
    private int id;
    private String name;
    private String imgTap;
    private String imgBoost;
    private String imgUrl;
}