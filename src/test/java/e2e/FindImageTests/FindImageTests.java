package e2e.FindImageTests;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import src.Application;
import src.dto.response.MatchImageResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class FindImageTests {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private String IMAGE_SCAN_URL = "/image/match/cat";
    
    @Test
    public void testApiCall() throws Exception {
        MatchImageResponse matchResponse = postApi(IMAGE_SCAN_URL, "++");
    }
    
    private MatchImageResponse postApi(String requestUrl, String requestBody) {
        String url = String.format("http://localhost:%d%s", port, requestUrl);
        ResponseEntity<MatchImageResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/image/match/cat", "++", MatchImageResponse.class);
        MatchImageResponse matchResponse = response.getBody();
        return matchResponse;
    }
}
