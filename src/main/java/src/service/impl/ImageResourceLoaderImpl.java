package src.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.enums.ErrorCode;
import src.enums.ImageType;
import src.model.Image;
import src.model.exceptions.GenericReaderException;
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

    private String getResourcePath(ImageType type) {
        String projectDirectory = System.getProperty("user.dir");
        String resourcePath = String.format("%s/src/main/resources/images/%s.txt", projectDirectory, type.getValue());
        return resourcePath;
    }
}
