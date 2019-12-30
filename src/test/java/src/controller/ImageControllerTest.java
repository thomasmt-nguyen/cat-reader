package src.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import src.Application;
import src.dto.response.MatchImageResponse;
import src.enums.ErrorCode;
import src.model.Match;
import src.model.exceptions.GenericReaderException;
import src.service.impl.ImageServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ImageController.class)
@ContextConfiguration(classes = {Application.class})
public class ImageControllerTest {
    
    @MockBean
    private ImageServiceImpl imageService;
    
    @Autowired
    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;
    
    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }
    
    @Test
    public void testFindZeroMatches() throws Exception{

        MatchImageResponse expectedResponse = new MatchImageResponse();
        expectedResponse.setMatches(new ArrayList<>());
        expectedResponse.setMessage("Found 0 matches");
        
        String body = "**";
        
        when(imageService.process(any(), any())).thenReturn(new ArrayList<>());
        String response = mockMvc.perform(post("/image/match/cat")
                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        Assert.assertEquals(response, expectedResponseString);
    }

    @Test
    public void testFindTwoMatches() throws Exception{

        MatchImageResponse expectedResponse = new MatchImageResponse();
        expectedResponse.setMatches(getMatchesList());
        expectedResponse.setMessage("Found 2 matches");

        String body = "**";

        when(imageService.process(any(), any())).thenReturn(getMatchesList());
        String response = mockMvc.perform(post("/image/match/cat")
                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        Assert.assertEquals(response, expectedResponseString);
    }

    @Test
    public void testExceptionThrown() throws Exception{
        GenericReaderException exception = GenericReaderException.builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
                .message("Error Message")
                .build();
        
        String body = "**";

        when(imageService.process(any(), any())).thenThrow(exception);
        mockMvc.perform(post("/image/match/cat")
                .content(body))
                .andExpect(status().is5xxServerError());
    }
    
    private List<Match> getMatchesList() {
        List<Match> matches = new ArrayList<>();
        
        Match match = Match.builder()
                .x(10)
                .y(20)
                .confidence(80)
                .build();
        
        matches.add(match);
        
        match = Match.builder()
                .x(40)
                .y(60)
                .confidence(90)
                .build();

        matches.add(match);
        return matches;
    }
}
