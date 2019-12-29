package src.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Match {
    private int x;
    private int y;
    double confidence;
}
