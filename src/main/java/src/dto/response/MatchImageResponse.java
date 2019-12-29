package src.dto.response;

import lombok.Data;
import src.model.Match;

import java.util.List;

@Data
public class MatchImageResponse {
    String message;
    List<Match> matches;
}
