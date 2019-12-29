package src.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import src.enums.ImageType;
import src.model.Image;
import src.model.exceptions.GenericReaderException;
import src.service.impl.ImageConverterImpl;
import src.service.impl.ImageResourceLoaderImpl;


import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageResourceLoadImplTest {
    
    ImageResourceLoaderImpl imageResourceLoader;
    
    @Mock
    private ImageConverterImpl imageConverter;
    
    @Before
    public void setup() {
        imageResourceLoader = new ImageResourceLoaderImpl(imageConverter);
    }
    
    @Test
    public void testGetPerfectImage() throws Exception {
        Image expectedImage = getPerfectImage();
        when(imageConverter.convert(anyString())).thenReturn(expectedImage);
        Image image = imageResourceLoader.getPerfectImage(ImageType.CAT.getValue());
        Assert.assertEquals(image, expectedImage);
    }

    @Test
    public void testException() {
        assertThatThrownBy(() -> imageResourceLoader.getPerfectImage("dog"))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Could not read file from path.");
    }
    
    private Image getPerfectImage() throws Exception {
        Image image = new Image();
        image.setHeight(15);
        image.setWidth(15);
        image.setGraph(getGraph());
        return image;
    }
    
    
    private char[][] getGraph() throws Exception{
        String projectDirectory = System.getProperty("user.dir");
        String resourcePath = String.format("%s/src/test/resources/%s.txt", projectDirectory, ImageType.CAT.getValue());
        String imageText = new String(Files.readAllBytes(Paths.get(resourcePath)));
        String[] splitImage = imageText.split("\\n");
        
        char [][] graph = new char[15][];
        for (int index = 0; index < 15; index++) {
            graph[index] = String.format("%-15s", splitImage[index], ' ').toCharArray();
        }
        
        return graph;
    }
}
