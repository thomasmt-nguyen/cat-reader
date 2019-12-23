package src.service;

import src.enums.ImageType;
import src.model.Image;

public interface ImageResourceLoader {
    Image getPerfectImage(ImageType type);
}
