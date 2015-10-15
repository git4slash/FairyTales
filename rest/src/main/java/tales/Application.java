// tag::runner[]
package tales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	@Bean
	CommandLineRunner init(AccountRepository accountRepository,
			TaleRepository TaleRepository) {
		return (evt) -> Arrays.asList(
				"jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
				.forEach(
						a -> {
							Account account = accountRepository.save(new Account(a,
									"password"));
							TaleRepository.save(new Tale(account,
									"http://Tale.com/1/" + a, "A text from tale1", "Tale1 from " + a));
							TaleRepository.save(new Tale(account,
                                    "http://Tale.com/2/" + a, "A text from tale2", "Tale2 from " + a));
							TaleRepository.save(new Tale(account,
                                    "http://Tale.com/3/" + a, "A text from tale3", "Tale3 from " + a));
							TaleRepository.save(new Tale(account,
                                    "http://Tale.com/4/" + a, "A text from tale4", "Tale4 from " + a));
							TaleRepository.save(new Tale(account,
                                    "http://Tale.com/5/" + a, "A text from tale5", "Tale5 from " + a));
						});
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
// end::runner[]

@RestController
@RequestMapping("/{userId}/tales")
class TaleRestController {

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
