package src.service;

import src.model.Coordinate;

import java.util.List;

public interface ImageService {
    List<Coordinate> process(String image, String type);
}
