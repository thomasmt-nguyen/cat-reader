package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.service.impl.ImageServiceImpl;

@RestController
@RequestMapping(value = "/")
public class ImageController {

    private ImageServiceImpl imageService;

    @Autowired
    ImageController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "image/process/{type}")
    public int image(@RequestBody String imageText, @PathVariable("type") String type) {
        return imageService.process(imageText, type);
    }
}
