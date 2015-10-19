package tales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

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
                    System.out.printf("User %s posted new Tale id:%s, name:%s", userId, input.getId(), input.getName());
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }).get();
    }

    @RequestMapping(value = "/{taleId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteTale(@PathVariable String userId, @PathVariable Long taleId) {
        this.validateUser(userId);
        Account account = accountRepository.findByUsername(userId).get();
        Tale tale = taleRepository.findOne(taleId);
        if (account.getId().equals(tale.getAccount().getId())) {
            taleRepository.delete(taleId);
            System.out.println("Tale id=" + taleId + " deleted by user " + userId);
        } else {
            account.unSubscribeFrom(tale);
            accountRepository.saveAndFlush(account);
            System.out.printf("User %s unsubscribed from Tale id:%s\n", userId, taleId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Tale> getAllTales(@PathVariable String userId) {
        this.validateUser(userId);
        Set<Tale> tales = accountRepository.findByUsername(userId).get().getSubscribedTales();
        tales.addAll(this.taleRepository.findByAccountUsername(userId));
        System.out.printf("getAllTales request by:%s\n", userId);
        return tales;
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

