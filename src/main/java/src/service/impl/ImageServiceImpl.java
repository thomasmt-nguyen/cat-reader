package src.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.enums.ImageType;
import src.model.Image;
import src.service.ImageConverter;
import src.service.ImageResourceLoader;
import src.service.ImageService;

import java.util.HashMap;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    private ImageConverter imageConverter;

    private ImageResourceLoader imageResourceLoader;

    @Autowired
    ImageServiceImpl(ImageConverterImpl imageConverter,
                     ImageResourceLoaderImpl imageResourceLoader) {
        this.imageConverter = imageConverter;
        this.imageResourceLoader = imageResourceLoader;
    }

    public int process(String imageText, String type) {
        
        //TODO: Throw error if type doesn't exists
        
        ImageType imageType = ImageType.valueOf(type.toUpperCase());
        Image requestImage = imageConverter.convert(imageText);
        Image perfectImage = imageResourceLoader.getPerfectImage(imageType);
        
        // TODO: Throw error if image is smaller than scan image
        return scan(requestImage, perfectImage);
    }
    
    private int scan(Image requestImage, Image perfectImage) {
        
        Map<Integer, Integer> matchLocation = new HashMap<>();
        int matchCounter = 0;

        for (int requestY = 0; requestY < requestImage.getHeight() - perfectImage.getHeight(); requestY++) {
            for (int requestX = 0; requestX < requestImage.getWidth() - perfectImage.getWidth(); requestX++) {
                boolean isMatch = isMatchScan(requestX, requestY, requestImage, perfectImage);
                matchCounter += isMatch ? 1 : 0;
                
                //TODO: Add match location and match %
                /*if (isMatch)
                    matchLocation.(new Map(requestX, requestY))*/
            }
        }
        
        return matchCounter;
    }
    
    private boolean isMatchScan(int requestX, int requestY, Image requestImage, Image perfectImage) {

        final int FINAL_IMAGE_THRESH_HOLD = getFinalScanThreshold(perfectImage);
        char[][] perfectGraph = perfectImage.getGraph();
        char[][] requestGraph = requestImage.getGraph();
        int pixelMatches = 0;
        
        //TODO: count backwards and return when the incorrect pixels is under threshold
        
        for (int perfectY = 0; perfectY < perfectImage.getHeight(); perfectY++) {
            for (int perfectX = 0; perfectX < perfectImage.getWidth(); perfectX++) {
                char requestPixel = requestGraph[requestY + perfectY][requestX + perfectX];
                char perfectPixel = perfectGraph[perfectY][perfectX];
                pixelMatches += requestPixel == perfectPixel ? 1 : 0;
            }
        }

        return pixelMatches > FINAL_IMAGE_THRESH_HOLD;
    }
    
    private int getFinalScanThreshold(Image image){
        double width = image.getWidth();
        double height = image.getHeight();
        double FINAL_SCAN_THRESH_HOLD = .85;
        return calculateThreshold(width, height, FINAL_SCAN_THRESH_HOLD);
    }
    
    private int calculateThreshold(double width, double height, double threshold) {
        return (int)(width * height * threshold);
    }
}
