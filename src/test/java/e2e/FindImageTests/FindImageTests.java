package e2e.FindImageTests;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import src.Application;
import src.dto.response.MatchImageResponse;
import src.model.Match;

import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class FindImageTests {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private Logger logger = LoggerFactory.getLogger(FindImageTests.class);
    
    private String MATCH_IMAGE_URL = "/image/match/cat";
    
    @Test
    public void testFindImage()  {
        String body = getTestRequestImage();
        MatchImageResponse matchResponse = post(MATCH_IMAGE_URL, body);
        
        int expectedMatchSize = 6;
        Assert.assertEquals(expectedMatchSize, matchResponse.getMatches().size());
        Assert.assertEquals("Found 6 matches", matchResponse.getMessage());
    }
    
    private MatchImageResponse post(String requestUrl, String requestBody) {
        String url = String.format("http://localhost:%d%s", port, requestUrl);
        ResponseEntity<MatchImageResponse> response = restTemplate.postForEntity(url, requestBody, MatchImageResponse.class);
        MatchImageResponse matchResponse = response.getBody();
        return matchResponse;
    }
    
    private String getTestRequestImage() {
        try {
            String projectDirectory = System.getProperty("user.dir");
            String resourcePath = String.format("%s/src/test/resources/test.txt", projectDirectory);
            return new String(Files.readAllBytes(Paths.get(resourcePath)));
        } catch (Exception exception) {
            logger.info("Error opening file exception={}", exception.getMessage());
            return null;
        }
    }
}
