package src.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.dto.response.MatchImageResponse;
import src.model.Match;
import src.service.impl.ImageServiceImpl;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class ImageController {

    private Logger logger = LoggerFactory.getLogger(ImageController.class);

    private ImageServiceImpl imageService;

    @Autowired
    ImageController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "image/match/{type}")
    public MatchImageResponse findMatches(@RequestBody String requestedImageBody, @PathVariable("type") String requestedType) {
        logger.info("Scanning requested image");
        List<Match> matches = imageService.process(requestedImageBody, requestedType);
        logger.info("Finished scanning requested image");
        MatchImageResponse response = new MatchImageResponse();
        response.setMatches(matches);
        response.setMessage(buildMessage(matches));
        return response;
    }

    private String buildMessage(List<Match> matches) {
        return String.format("Found %d matches", matches.size());
    }
}
