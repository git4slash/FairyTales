// tag::runner[]
package tales;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	@Bean
	CommandLineRunner fillDB(AccountRepository accountRepository,
			TaleRepository taleRepository) {

        Account spammer = accountRepository.save(new Account("spammer", "pass"));
        Tale spamTale = taleRepository.save(new Tale(spammer,
                "spam link", "SPAM spam", "You want be reach?"));

		return (evt) -> Arrays.asList(
				"jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
				.forEach(
                        a -> {
                            // creating account in db
                            Account account = accountRepository.save(new Account(a,
                                    "password"));

                            // creating tale for current account (acc is author from this tales)
                            taleRepository.save(new Tale(account,
                                    "http://Tale.com/1/" + a, "A text from tale1", "Tale1 from " + a));
                            taleRepository.save(new Tale(account,
                                    "http://Tale.com/2/" + a, "A text from tale2", "Tale2 from " + a));
                            taleRepository.save(new Tale(account,
                                    "http://Tale.com/3/" + a, "A text from tale3", "Tale3 from " + a));
                            taleRepository.save(new Tale(account,
                                    "http://Tale.com/4/" + a, "A text from tale4", "Tale4 from " + a));
                            taleRepository.save(new Tale(account,
                                    "http://Tale.com/5/" + a, "A text from tale5", "Tale5 from " + a));

                            // subscribing on spam (acc is subscriber)
                            account.subscribeOn(spamTale);
                        });
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
// end::runner[]