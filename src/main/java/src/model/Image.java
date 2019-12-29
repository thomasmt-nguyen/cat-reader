package src.model;

import lombok.Data;

@Data
public class Image {
    private char [][] graph;
    private double width;
    private double height;

    public double getTotalPixels() {
        return width * height;
    }
}
