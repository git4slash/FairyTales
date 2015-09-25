package farytale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "story")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORY_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "STORY_NAME", unique = false)
    private String storyName;

    @Column(name = "STORY_TEXT", length = 510)
    private String storyText;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Account> subscreibers;

    @JsonIgnore
    @ManyToOne
    private Account author;

    public Story() {
    }

    public Story(String storyName, String storyText, Account author) {
        this.storyName = storyName;
        this.storyText = storyText;
        this.author = author;
    }


    public Set<Account> getSubscreibers() {return subscreibers;}

    public Long getId() {
        return id;
    }

    public Account getAuthor() {return author;}

    public String getStoryName() {
        return storyName;
    }

    public String getStoryText() {
        return storyText;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", storyName='" + storyName + '\'' +
                ", storyText='" + storyText + '\'' +
                ", author=" + author.getUsername() +
                '}';
    }
}