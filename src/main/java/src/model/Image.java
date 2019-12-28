package src.model;

import lombok.Data;

@Data
public class Image {
    private char [][] graph;
    private int width;
    private int height;

    public int getTotalPixels() {
        return width * height;
    }
}
