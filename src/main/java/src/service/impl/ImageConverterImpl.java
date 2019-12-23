package src.service.impl;

import org.springframework.stereotype.Service;
import src.model.Image;
import src.service.ImageConverter;

@Service
public class ImageConverterImpl implements ImageConverter {

    public Image convert(String imageText) {
        String[] splitImage = imageText.split("\\n");
        int imageWidth = getImageWidth(splitImage);
        String format = getFormat(imageWidth);

        char [][] graph = new char[splitImage.length][];

        for (int index = 0; index < splitImage.length; index++) {
            graph[index] = String.format(format, splitImage[index], ' ').toCharArray();
        }
        
        Image image = new Image();
        image.setGraph(graph);
        image.setWidth(imageWidth);
        image.setHeight(splitImage.length);
        
        return image;
    }

    private String getFormat(int length) {
        return "%-" + length + "s";
    }

    private int getImageWidth(String[] splitImage) {
        int maxWdith = 0;

        for (String line: splitImage) {
            if (line.length() > maxWdith)
                maxWdith = line.length();
        }

        return maxWdith;
    }
}
