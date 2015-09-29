package tales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tales.model.Account;
import tales.model.AccountRepository;

import java.util.List;

@RestController
public class UsersRestController {

    private final AccountRepository accountRepository;

    @Autowired
    public UsersRestController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping("/users")
    List<Account> users() {
        return accountRepository.findAll();
    }
}
