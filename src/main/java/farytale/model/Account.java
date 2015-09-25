package farytale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "USER_NAME", unique = true, nullable = false, length = 20)
    private String username;

    @JsonIgnore
    @Column(name = "USER_PASSWORD",nullable = false, length = 10)
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "subscriptions",
            joinColumns = {@JoinColumn(name = "STORY_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "ACCOUNT_ID", nullable = false, updatable = false)})
    private Set<Story> subscribedStories = new HashSet<>();

    @JsonIgnore
    @OneToMany
    private Set<Story> myStories = new HashSet<>();

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Story> getStories() {
        return myStories;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", id=" + id +
                '}';
    }

    public Set<Story> getSubscribedStories() {
        return subscribedStories;
    }

    public void subscribeOnStory(Story story) {
        this.myStories.add(story);
    }
}
