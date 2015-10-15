package tales;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TaleRestControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String userName = "bdussault";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Account testAccount;

    private List<Tale> tales = new ArrayList<>();

    @Autowired
    private TaleRepository taleRepository;

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
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.taleRepository.deleteAllInBatch();
        this.accountRepository.deleteAllInBatch();

        this.testAccount = accountRepository.save(new Account(userName, "password"));
        this.tales.add(taleRepository.save(new Tale(
                testAccount,
                "http://localhost/" + userName + "/tales/1",
                "Tale text",
                "Tale name")));
        this.tales.add(taleRepository.save(new Tale(
                testAccount,
                "http://localhost/" + userName + "/tales/2",
                "Tale text",
                "Tale name")));
    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(post("/jemand/tales/")
                .content(this.json(new Tale()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    // test GET from host/userName/tales/
    @Test
    public void testReadUserTales() throws Exception {
        mockMvc.perform(get("/" + userName + "/tales"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(tales.size())))
                .andExpect(jsonPath("$[0].id", is(tales.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].uri", is(tales.get(0).getUri())))
                .andExpect(jsonPath("$[0].text", is(tales.get(0).getText())))
                .andExpect(jsonPath("$[0].name", is(tales.get(0).getName())))
                .andExpect(jsonPath("$[1].id", is(tales.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].uri", is(tales.get(1).getUri())))
                .andExpect(jsonPath("$[1].text", is(tales.get(1).getText())))
                .andExpect(jsonPath("$[1].name", is(tales.get(1).getName())));
    }

    // test for POST method for address: host/userName/tales
    @Test
    public void testCreateNewTale() throws Exception {
        String taleJson = json(new Tale(
                testAccount,
                "http://localhost/" + testAccount.getUsername() + "/tales/3",
                "Tale number 3 text",
                "Name for tale #3"));

        this.mockMvc.perform(post("/" + userName + "/tales")
                                .contentType(contentType)
                                .content(taleJson))
                .andExpect(status().isCreated());
    }

    // test DELETE from host/userName/tales/{taleId}
    @Test
    public void testDeleteTale() throws Exception {
        mockMvc.perform(delete("/" + userName + "/tales/" + tales.get(0).getId()))
                .andExpect(status().isOk());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
