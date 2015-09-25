package farytale;

import farytale.model.Account;
import farytale.model.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@ComponentScan
@Configuration
public class Application implements CommandLineRunner {

    // todo export on server!
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    AccountRepository accountRepository;
    @Override
    public void run(String... strings) throws Exception {
        accountRepository.save(new Account("TestUser", "pas"));
        accountRepository.save(new Account("Allll", "pas"));
        accountRepository.save(new Account("Sla", "pas"));
        accountRepository.save(new Account("bla", "pas"));
        accountRepository.findAll().forEach(System.out::println);
    }

//    @Bean
//    CommandLineRunner init(AccountRepository accountRepository,
//                           StoryRepository storyRepository)
//    {
//        return (evt) -> Arrays.asList(
//                "Mathias,Kirsten,Ruben,Till,Alex".split(","))
//                .forEach( a -> {
//                    Account account = accountRepository.save(new Account(a, "pass"));
//
//                    storyRepository.save(new Story("Story from " + account.getUsername(),
//                            "Entertaining and enlightening text. Happy end.", account));
//
//                    storyRepository.save(new Story("Story2 from " + account.getUsername(),
//                            "Story Text...", account));
//                });
//    }
}