package src.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coordinate {
    private int x;
    private int y;
}
