package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.dto.response.ScanImageResponse;
import src.model.Coordinate;
import src.service.impl.ImageServiceImpl;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class ImageController {

    private ImageServiceImpl imageService;

    @Autowired
    ImageController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "image/scan/{type}")
    public ScanImageResponse scanImage(@RequestBody String requestedImageBody, @PathVariable("type") String requestedType) {
        List<Coordinate> coordinates = imageService.process(requestedImageBody, requestedType);
        ScanImageResponse response = new ScanImageResponse();
        response.setCoordinates(coordinates);
        return response;
    }
}
