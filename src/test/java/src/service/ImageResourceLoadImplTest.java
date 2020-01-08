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
import src.factory.impl.ImageFactoryImpl;
import src.service.impl.ImageResourceLoaderImpl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageResourceLoadImplTest {
    
    ImageResourceLoaderImpl imageResourceLoader;
    
    @Mock
    private ImageFactoryImpl imageConverter;
    
    @Before
    public void setup() {
        imageResourceLoader = new ImageResourceLoaderImpl(imageConverter);
    }
    
    @Test
    public void testGetPerfectImage(){
        Image expectedImage = getPerfectImage();
        when(imageConverter.createImage(anyString())).thenReturn(expectedImage);
        Image image = imageResourceLoader.getPerfectImage(ImageType.CAT.getValue());
        Assert.assertEquals(image, expectedImage);
    }

    @Test
    public void testException() {
        assertThatThrownBy(() -> imageResourceLoader.getPerfectImage("dog"))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Could not read file from path.");
    }
    
    private Image getPerfectImage(){
        Image image = new Image();
        image.setHeight(2);
        image.setWidth(2);
        image.setGraph(getGraph());
        return image;
    }
    
    
    private char[][] getGraph() {
        char[][] graph = new char[2][2];
        graph[0] = ("++").toCharArray();
        graph[1] = ("++").toCharArray();
        return graph;
    }
}
