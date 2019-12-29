package src.dto.response;

import lombok.Data;
import src.model.Match;

import java.util.List;

@Data
public class ScanImageResponse {
    List<Match> matches;
}
