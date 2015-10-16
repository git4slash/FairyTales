package tales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/{userId}/subscriptions")
public class SubscriptionsRestController {

    private final TaleRepository taleRepository;

    private final AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> subscribeOnTale(@PathVariable String userId, @RequestBody Tale input) {
        this.validateUser(userId);
        return this.accountRepository
                .findByUsername(userId)
                .map(account -> {
                    account.subscribeOn(taleRepository.findOne(input.getId()));

                    System.out.printf("User %s subscribed on %s", userId, input.getName());
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }).get();
    }

    @RequestMapping(value = "/{taleId}", method = RequestMethod.DELETE)
    ResponseEntity<?> unSubscribeFromTale(@PathVariable String userId, @PathVariable Long taleId) {
        this.validateUser(userId);
        return this.accountRepository
                .findByUsername(userId)
                .map(account -> {
                    account.unSubscribeFrom(taleRepository.findOne(taleId));

                    System.out.printf("User %s unsubscribed from Tale id:%s", userId, taleId);
                    return new ResponseEntity<>(HttpStatus.OK);
                }).get();
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Tale> getSubscribedTales(@PathVariable String userId) {
        this.validateUser(userId);
        Set<Tale> answer = this.accountRepository.findByUsername(userId).get().getSubscribedTales();
        System.out.printf("User %s requested subscriptions. Returned %s tales", userId, answer.size());
        return answer;
    }

    @Autowired
    SubscriptionsRestController(TaleRepository TaleRepository,
                                AccountRepository accountRepository) {
        this.taleRepository = TaleRepository;
        this.accountRepository = accountRepository;
    }

    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}
