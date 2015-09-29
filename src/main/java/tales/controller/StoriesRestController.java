package tales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tales.model.Story;
import tales.model.StoryRepository;

import java.util.List;

@RestController
public class StoriesRestController {
    private final StoryRepository storyRepository;

    @Autowired
    public StoriesRestController(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @RequestMapping("/stories")
    List<Story> readAllStoryNames() {
        return this.storyRepository.findAll();
    }
}
