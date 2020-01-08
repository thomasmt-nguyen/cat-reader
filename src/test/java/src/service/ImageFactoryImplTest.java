package src.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import src.model.Image;
import src.model.exceptions.GenericReaderException;
import src.factory.impl.ImageFactoryImpl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImageFactoryImplTest {

    ImageFactoryImpl imageConverter;
    
    @Before
    public void setup() {
        imageConverter = new ImageFactoryImpl();
    }
    
    @Test
    public void testConvert() {
        String imageText = getImperfectImageText();
        Image expectedImage = getPerfectImage();
        Image image = imageConverter.createImage(imageText);
        Assert.assertEquals(image, expectedImage);
    }
    
    @Test
    public void testWhiteSpacesCenter() {
        String imageText = "++\n" +
                           "++\n";
        Image expectedImage = getPerfectImage();
        Image image = imageConverter.createImage(imageText);
        Assert.assertNotEquals(image, expectedImage);
    }
    
    @Test
    public void testEmptyMatchSearch() {
        assertThatThrownBy(() -> imageConverter.createImage(""))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Cannot create an empty image");
    }

    @Test
    public void testEmptyMatchSearchNull() {
        assertThatThrownBy(() -> imageConverter.createImage(null))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Cannot create an empty image");
    }
    
    @Test
    public void testSinglePixel() {
        String imageText = "+";
        Image expectedImage = getPerfectSinglePixelImage();
        Image image = imageConverter.createImage(imageText);
        Assert.assertEquals(image, expectedImage);
        
    }

    @Test
    public void testSingleLineImage() {
        String imageText = "++";
        Image expectedImage = getPerfectSingleLineImage();
        Image image = imageConverter.createImage(imageText);
        Assert.assertEquals(expectedImage, image);

    }
    
    private String getImperfectImageText() {
        String imperfectText = "++\n" +
                               "++\n" +
                               "+++\n";
        return imperfectText;
    }

    private Image getPerfectImage() {
        Image image = new Image();
        
        char[][] imperfectText = new char[3][3];
        imperfectText[0] = "++ ".toCharArray();
        imperfectText[1] = "++ ".toCharArray();
        imperfectText[2] = "+++".toCharArray();
        
        image.setWidth(3);
        image.setHeight(3);
        image.setGraph(imperfectText);
        return image;
    }

    private Image getPerfectSinglePixelImage() {
        Image image = new Image();

        char[][] imperfectText = new char[1][1];
        imperfectText[0] = "+".toCharArray();

        image.setWidth(1);
        image.setHeight(1);
        image.setGraph(imperfectText);
        return image;
    }

    private Image getPerfectSingleLineImage() {
        Image image = new Image();

        char[][] imperfectText = new char[1][2];
        imperfectText[0] = "++".toCharArray();
        image.setWidth(2);
        image.setHeight(1);
        image.setGraph(imperfectText);
        return image;
    }
    
}
