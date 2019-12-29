package src.service;

import src.model.Match;

import java.util.List;

public interface ImageService {
    List<Match> process(String image, String type);
}
