package src.dto.response;

import lombok.Data;
import src.model.Coordinate;

import java.util.List;

@Data
public class ScanImageResponse {
    List<Coordinate> coordinates;
}
