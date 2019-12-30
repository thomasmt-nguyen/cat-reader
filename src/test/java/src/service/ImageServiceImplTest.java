package src.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import src.enums.ErrorCode;
import src.model.Image;
import src.model.Match;
import src.model.exceptions.GenericReaderException;
import src.service.impl.ImageConverterImpl;
import src.service.impl.ImageResourceLoaderImpl;
import src.service.impl.ImageServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {
    
    private ImageServiceImpl imageService;
    
    @Mock
    private ImageConverterImpl imageConverter;
    
    @Mock
    private ImageResourceLoaderImpl resourceLoader;
    
    @Before
    public void setup() {
        imageService = new ImageServiceImpl(imageConverter, resourceLoader);
    }
    
    @Test
    public void testProcess() {
        when(imageConverter.convert(anyString())).thenReturn(getRequestImage());
        when(resourceLoader.getPerfectImage(anyString())).thenReturn(getSquareImage());
        
        List<Match> matches = imageService.process(getImageRequestBody(), "CAT");
        Assert.assertEquals(2, matches.size());
        
        Match match = matches.get(0);
        
        Assert.assertEquals(0, match.getX());
        Assert.assertEquals(0, match.getY());
        Assert.assertEquals(100, match.getConfidence(), .0);

        match = matches.get(1);
        Assert.assertEquals(1, match.getX());
        Assert.assertEquals(1, match.getY());
        Assert.assertEquals(100, match.getConfidence(), .0);
    }
    
    @Test
    public void testException() {
        GenericReaderException exception = GenericReaderException.builder()
                .errorCode(ErrorCode.EMPTY_IMAGE_TEXT)
                .message("Cannot convert an empty image")
                .build();
        
        when(imageConverter.convert(anyString())).thenThrow(exception);

        assertThatThrownBy(()-> imageService.process("", "CAT"))
                .isInstanceOf(GenericReaderException.class)
                .hasMessage("Cannot convert an empty image");
        
        verifyNoMoreInteractions(resourceLoader);
    }

    @Test
    public void testNotValidTypeException() {
        assertThatThrownBy(()-> imageService.process(getImageRequestBody(), "dog"))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Invalid requested image type.");

        verifyZeroInteractions(imageConverter);
        verifyZeroInteractions(resourceLoader);
    }
    
    @Test
    public void testNotValidSizeHeightException() {

        when(imageConverter.convert(anyString())).thenReturn(getSingleLineRequestImage());
        when(resourceLoader.getPerfectImage(anyString())).thenReturn(getSquareImage());
        
        assertThatThrownBy(()-> imageService.process("++", "CAT"))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Request body is smaller than the requested search image");
    }

    @Test
    public void testNotValidSizeWidthException() {
        
        Image image = getSingleLineRequestImage();
        image.setHeight(3);
        image.setWidth(1);

        when(imageConverter.convert(anyString())).thenReturn(getSingleLineRequestImage());
        when(resourceLoader.getPerfectImage(anyString())).thenReturn(getSquareImage());

        assertThatThrownBy(()-> imageService.process("++", "CAT"))
                .isInstanceOf(GenericReaderException.class)
                .hasMessageContaining("Request body is smaller than the requested search image");
    }

    @Test
    public void testSameSizeShouldReturnOneMatch() {
        when(imageConverter.convert(anyString())).thenReturn(getSquareImage());
        when(resourceLoader.getPerfectImage(anyString())).thenReturn(getSquareImage());

        List<Match> matches = imageService.process("++\n++\n", "CAT");
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals(100, matches.get(0).getConfidence(), .00);
    }

    @Test
    public void testShouldReturnOneMatchLarger() {
        when(imageConverter.convert(anyString())).thenReturn(getRequestImage2());
        when(resourceLoader.getPerfectImage(anyString())).thenReturn(getLargerSquareImage());

        List<Match> matches = imageService.process("++ +\n+++\n+++ \n  ++", "CAT");
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals(88.88, matches.get(0).getConfidence(), .00);
    }
    
    private Image getSingleLineRequestImage() {
        Image image = new Image();

        char[][] graph = new char[1][2];
        graph[0] = "++".toCharArray();

        image.setGraph(graph);
        image.setHeight(1);
        image.setWidth(2);

        return image;
    }

    private Image getSquareImage() {
        Image image = new Image();

        char[][] graph = new char[2][2];
        graph[0] = "++".toCharArray();
        graph[1] = "++".toCharArray();
        
        image.setGraph(graph);
        image.setHeight(2);
        image.setWidth(2);
        
        return image;
    }

    private Image getLargerSquareImage() {
        Image image = new Image();

        char[][] graph = new char[3][3];
        graph[0] = "+++".toCharArray();
        graph[1] = "+++".toCharArray();
        graph[2] = "+++".toCharArray();

        image.setGraph(graph);
        image.setHeight(3);
        image.setWidth(3);

        return image;
    }
    
    private String getImageRequestBody() {
        String request = "++ +\n+++ \n ++\n  ++\n";
        return request;
    }

    private Image getRequestImage() {
        Image image = new Image();

        char[][] graph = new char[4][4];
        graph[0] = "++ +".toCharArray();
        graph[1] = "+++ ".toCharArray();
        graph[2] = " ++ ".toCharArray();
        graph[3] = "  ++".toCharArray();     
        
        image.setGraph(graph);
        image.setHeight(4);
        image.setWidth(4);

        return image;
    }

    private Image getRequestImage2() {
        Image image = new Image();

        char[][] graph = new char[4][4];
        graph[0] = "++ +".toCharArray();
        graph[1] = "+++ ".toCharArray();
        graph[2] = "+++ ".toCharArray();
        graph[3] = "  ++".toCharArray();

        image.setGraph(graph);
        image.setHeight(4);
        image.setWidth(4);

        return image;
    }
}
