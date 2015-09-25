package farytale.controller;

import farytale.model.AccountRepository;
import farytale.model.Story;
import farytale.model.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/{userId}/stories")
public class AccountRestController {

    private final AccountRepository accountRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public AccountRestController(AccountRepository accountRepository, StoryRepository storyRepository) {
        this.accountRepository = accountRepository;
        this.storyRepository = storyRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addStory(@PathVariable String userId, @RequestBody Story inputStory) {
        this.validateUser(userId);

        return this.accountRepository.findByUsername(userId)
                .map(account -> {
                    Story newStory = this.storyRepository.save(new Story(inputStory.getStoryName(),
                            inputStory.getStoryText(), account));
                    return new ResponseEntity<>(null, null, HttpStatus.CREATED);
                }).get();
    }

    @RequestMapping(value = "/{storyId}", method = RequestMethod.GET)
    Story readStory(@PathVariable String userId, @PathVariable Long storyId) {
        validateUser(userId);
        return this.storyRepository.findOne(storyId);
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Story> readStories(@PathVariable String userId) {
        this.validateUser(userId);
        return this.storyRepository.findByAuthorUsername(userId);
    }

    //TODO implement user-authentication logic
    private void validateUser(String userId) {

    }
}