package controller;

import farytale.Application;
import farytale.model.Account;
import farytale.model.AccountRepository;
import farytale.model.Story;
import farytale.model.StoryRepository;
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
 * Created by Slash on 22.09.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class StoryRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Story> stories = new ArrayList<>();
    private List<Account> authors = new ArrayList<>();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private AccountRepository authrorsRepository;

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

        this.authrorsRepository.deleteAllInBatch();

        //creating authors
        this.authors.add(this.authrorsRepository.save(new Account("Alex", "pass")));
        this.authors.add(this.authrorsRepository.save(new Account("Vika", "pass")));

        this.storyRepository.deleteAllInBatch();

        // creating stories
        this.stories.add(this.storyRepository.save(new Story("Story from Alex", "story text",
                this.authors.get(0))));
        this.stories.add(this.storyRepository.save(new Story("Story2 from Alex", "story text",
                this.authors.get(0))));
        this.stories.add(this.storyRepository.save(new Story("Story from Vika", "story text",
                this.authors.get(1))));
    }

    @Test
    public void testReadAllStoryNames() throws Exception {
        mockMvc.perform(get("/stories/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(this.stories.size())))
                .andExpect(jsonPath("$[0].id", is(this.stories.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].storyName", is(this.stories.get(0).getStoryName())))
                .andExpect(jsonPath("$[0].storyText", is(this.stories.get(0).getStoryText())))
                .andExpect(jsonPath("$[1].id", is(this.stories.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].storyName", is(this.stories.get(1).getStoryName())))
                .andExpect(jsonPath("$[1].storyText", is(this.stories.get(1).getStoryText())))
                .andExpect(jsonPath("$[2].id", is(this.stories.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].storyName", is(this.stories.get(2).getStoryName())))
                .andExpect(jsonPath("$[2].storyText", is(this.stories.get(2).getStoryText())))
 ;
    }
}