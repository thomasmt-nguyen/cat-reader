package src.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.enums.ErrorCode;
import src.factory.impl.ImageFactoryImpl;
import src.model.Image;
import src.model.exceptions.GenericReaderException;
import src.factory.ImageFactory;
import src.service.ImageResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageResourceLoaderImpl implements ImageResourceLoader {

    private ImageFactory imageFactory;

    @Autowired
    public ImageResourceLoaderImpl(ImageFactoryImpl imageConverter) {
        this.imageFactory = imageConverter;
    }

    public Image getPerfectImage(String type) {
        String resourcePath = getResourcePath(type);
        String imageText = readFile(resourcePath);
        return imageFactory.createImage(imageText);
    }

    private String readFile(String resourcePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(resourcePath)));
        }
        catch (IOException exception)
        {
            throw GenericReaderException.builder()
                    .errorCode(ErrorCode.READ_FILE_ERROR)
                    .message(String.format("Could not read file from path. path=%s exception=%s", resourcePath,
                            exception.getMessage()))
                    .build();
        }
    }

    private String getResourcePath(String type) {
        String projectDirectory = System.getProperty("user.dir");
        String resourcePath = String.format("%s/src/main/resources/images/%s.txt", projectDirectory, type);
        return resourcePath;
    }
}
