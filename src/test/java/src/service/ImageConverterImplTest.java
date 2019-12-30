package src.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import src.model.Image;
import src.model.exceptions.GenericReaderException;
import src.service.impl.ImageConverterImpl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImageConverterImplTest {

    ImageConverterImpl imageConverter;
    
    @Before
    public void setup() {
        imageConverter = new ImageConverterImpl();
    }
    
    @Test
    public void testConvert() {
        String imageText = getImperfectImageText();
        Image expectedImage = getPerfectImage();
        Image image = imageConverter.convert(imageText);
        Assert.assertEquals(image, expectedImage);
    }
    
    @Test
    public void testWhiteSpacesCenter() {
        String imageText = "++\n" +
                           "++\n";
        Image expectedImage = getPerfectImage();
        Image image = imageConverter.convert(imageText);
        Assert.assertNotEquals(image, expectedImage);
    }
    
    @Test
    public void testEmptyMatchSearch() {
        assertThatThrownBy(() -> imageConverter.convert(""))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Cannot convert an empty image");
    }

    @Test
    public void testEmptyMatchSearchNull() {
        assertThatThrownBy(() -> imageConverter.convert(null))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Cannot convert an empty image");
    }
    
    @Test
    public void testSinglePixel() {
        String imageText = "+";
        Image expectedImage = getPerfectSinglePixelImage();
        Image image = imageConverter.convert(imageText);
        Assert.assertEquals(image, expectedImage);
        
    }

    @Test
    public void testSingleLineImage() {
        String imageText = "++";
        Image expectedImage = getPerfectSingleLineImage();
        Image image = imageConverter.convert(imageText);
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
