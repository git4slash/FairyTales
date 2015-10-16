package tales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/{userId}/tales")
public class TaleRestController {

    private final TaleRepository taleRepository;

    private final AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> createNewTale(@PathVariable String userId, @RequestBody Tale input) {
        this.validateUser(userId);
        return this.accountRepository
                .findByUsername(userId)
                .map(account -> {
                    taleRepository.save(new Tale(account,
                            input.uri, input.text, input.name));

                    System.out.println(input.getName() + " Text: " + input.getText());
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }).get();
    }

    @RequestMapping(value = "/{taleId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteTale(@PathVariable String userId, @PathVariable Long taleId) {
        this.validateUser(userId);
        return this.accountRepository
                .findByUsername(userId)
                .map(account -> {
                    taleRepository.delete(taleId);
                    System.out.println("Tale id="+ taleId + " deleted by user "
                            + accountRepository.findByUsername(userId).get().getUsername());

                    return new ResponseEntity<>(HttpStatus.OK);
                }).get();
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Tale> readUserTales(@PathVariable String userId) {
        this.validateUser(userId);
        return this.taleRepository.findByAccountUsername(userId);
    }

    @Autowired
    TaleRestController(TaleRepository TaleRepository,
                       AccountRepository accountRepository) {
        this.taleRepository = TaleRepository;
        this.accountRepository = accountRepository;
    }

    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("could not find user '" + userId + "'.");
    }
}

