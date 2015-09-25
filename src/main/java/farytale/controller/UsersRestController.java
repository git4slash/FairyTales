package farytale.controller;

import farytale.model.Account;
import farytale.model.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersRestController {

    private final AccountRepository accountRepository;

    @Autowired
    public UsersRestController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Method return all known user names
     * @return List with all users
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    List<Account> users() {
        return accountRepository.findAll();
    }
}
