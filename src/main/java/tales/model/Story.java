package tales.model;

import javax.persistence.*;

@Entity
@Table(name = "stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "story_name", nullable = false)
    private String storyName;

    @Column(name = "story_text")
    private String storyText;

    @OneToOne(fetch = FetchType.LAZY)
    private Account author;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Collection<Account> readers;

    public Story() {
    }

    public Story(String storyName, String storyText, Account author) {
        this.storyName = storyName;
        this.storyText = storyText;
    }

    public Long getId() {
        return id;
    }

    public String getStoryName() {
        return storyName;
    }

    public String getStoryText() {
        return storyText;
    }

    @Override
    public String toString() {
        return "Story{" +
                "author=" + author +
                ", id=" + id +
                ", storyName='" + storyName + '\'' +
                ", storyText='" + storyText + '\'' +
                '}';
    }
}