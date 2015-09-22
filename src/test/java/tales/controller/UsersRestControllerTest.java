package tales.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import tales.Application;
import tales.model.Account;
import tales.model.AccountRepository;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Slash on 22.09.2015
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UsersRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Account> accounts = new ArrayList<>();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.accountRepository.deleteAllInBatch();
        this.accounts.add(this.accountRepository.save(new Account("Alex", "father")));
        this.accounts.add(this.accountRepository.save(new Account("Katya", "youngest daughter")));
        this.accounts.add(this.accountRepository.save(new Account("Nastya", "daughter")));
        accounts.forEach(System.out::println);
        accountRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void canFetchUsers() throws Exception {
        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(this.accountRepository.findAll().size())))
                .andExpect(jsonPath("$[0].id", is(this.accounts.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].username", is(this.accounts.get(0).getUsername())))
                .andExpect(jsonPath("$[1].id", is(this.accounts.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].username", is(this.accounts.get(1).getUsername())))
                .andExpect(jsonPath("$[2].id", is(this.accounts.get(2).getId().intValue()+1)))
                .andExpect(jsonPath("$[2].username", is(this.accounts.get(2).getUsername())));
    }
}