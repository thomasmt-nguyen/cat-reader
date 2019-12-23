package src.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.enums.ImageType;
import src.model.Image;
import src.service.ImageConverter;
import src.service.ImageResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageResourceLoaderImpl implements ImageResourceLoader {

    private ImageConverter imageConverter;

    @Autowired
    ImageResourceLoaderImpl(ImageConverterImpl imageConverter) {
        this.imageConverter = imageConverter;
    }

    public Image getPerfectImage(ImageType type) {
        String resourcePath = getResourcePath(type);
        String imageText = readFile(resourcePath);
        return imageConverter.convert(imageText);
    }

    private String readFile(String resourcePath) {
        try {
            String imageText = new String(Files.readAllBytes(Paths.get(resourcePath)));
            return imageText;
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    private String getResourcePath(ImageType type) {
        String projectDirectory = System.getProperty("user.dir");
        String resourcePath = String.format("%s/src/main/resources/images/%s.txt", projectDirectory, type.getValue());
        return resourcePath;
    }
}
