package src.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.enums.ErrorCode;
import src.enums.ImageType;
import src.model.Match;
import src.model.Image;
import src.model.exceptions.GenericReaderException;
import src.service.ImageConverter;
import src.service.ImageResourceLoader;
import src.service.ImageService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private ImageConverter imageConverter;

    private ImageResourceLoader imageResourceLoader;

    @Autowired
    public ImageServiceImpl(ImageConverterImpl imageConverter, ImageResourceLoaderImpl imageResourceLoader) {
        this.imageConverter = imageConverter;
        this.imageResourceLoader = imageResourceLoader;
    }

    public List<Match> process(String requestedImageBody, String requestedImageType) {

        ImageType imageType = translateImageType(requestedImageType);
        Image requestImage = imageConverter.convert(requestedImageBody);
        Image perfectImage = imageResourceLoader.getPerfectImage(imageType.getValue());
        validateRequestImage(requestImage, perfectImage);

        return findImage(requestImage, perfectImage);
    }

    private ImageType translateImageType(String type) {
        try {
            return ImageType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw GenericReaderException.builder()
                    .errorCode(ErrorCode.INVALID_IMAGE_TYPE)
                    .message(String.format("Invalid requested image type. imageType=%s", type))
                    .build();
        }
    }

    private void validateRequestImage(Image requestImage, Image perfectImage) {
        if (requestImage.getWidth() < perfectImage.getWidth() || requestImage.getHeight() < perfectImage.getHeight()) {
            throw GenericReaderException.builder()
                    .errorCode(ErrorCode.INVALID_IMAGE_SIZE)
                    .message("Request body is smaller than the requested search image")
                    .build();
        }
    }
    
    private List<Match> findImage(Image requestImage, Image perfectImage) {
        
        List<Match> matchLocations = new ArrayList<>();
        final int minimumThreshold = calculateAcceptableThreshold(perfectImage);

        for (int requestY = 0; requestY <= requestImage.getHeight() - perfectImage.getHeight(); requestY++) {
            for (int requestX = 0; requestX <= requestImage.getWidth() - perfectImage.getWidth(); requestX++) {
                double matchedPixels = executePerfectImageScan(requestX, requestY, requestImage, perfectImage, minimumThreshold);
                if (matchedPixels > minimumThreshold) {
                    double matchPercentage = BigDecimal.valueOf((matchedPixels / perfectImage.getTotalPixels()) * 100)
                            .setScale(2, RoundingMode.DOWN)
                            .doubleValue();

                    Match match = Match.builder()
                            .x(requestX)
                            .y(requestY)
                            .confidence(matchPercentage)
                            .build();

                    matchLocations.add(match);
                }
            }
        }

        if (matchLocations.isEmpty()) {
            logger.info("No successful matches for requested image");
        } else {
            logger.info("Successfully matched images matches={}", matchLocations);
        }
        
        return matchLocations;
    }
    
    private double executePerfectImageScan(int requestX, int requestY, Image requestImage, Image perfectImage, int minimumThreshold) {

        char[][] perfectGraph = perfectImage.getGraph();
        char[][] requestGraph = requestImage.getGraph();
        double remainingPixelThreshold = perfectImage.getTotalPixels();

        for (int perfectY = 0; perfectY < perfectImage.getHeight() && remainingPixelThreshold > minimumThreshold; perfectY++) {
            for (int perfectX = 0; perfectX < perfectImage.getWidth() && remainingPixelThreshold > minimumThreshold; perfectX++) {
                char requestPixel = requestGraph[requestY + perfectY][requestX + perfectX];
                char perfectPixel = perfectGraph[perfectY][perfectX];
                remainingPixelThreshold -= requestPixel == perfectPixel ? 0 : 1;
            }
        }

        return remainingPixelThreshold;
    }
    
    private int calculateAcceptableThreshold(Image image){
        double width = image.getWidth();
        double height = image.getHeight();
        double ACCEPTABLE_THRESH_HOLD = .85;
        return calculateThreshold(width, height, ACCEPTABLE_THRESH_HOLD);
    }
    
    private int calculateThreshold(double width, double height, double threshold) {
        return (int)(width * height * threshold);
    }
}
