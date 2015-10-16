package tales;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tale {

    @JsonIgnore
    @ManyToOne
    private Account account;

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToMany(targetEntity = Account.class)
    Set<Account> subscribers = new HashSet<>();

    public String uri;
    public String name;
    public String text;

    Tale() {}

    public Tale(Account account, String uri, String text) {
        this.uri = uri;
        this.text = text;
        this.account = account;
    }

    public Tale(Account account, String uri, String taleText, String taleName) {
        this.account = account;
        this.uri = uri;
        this.name = taleName;
        this.text = taleText;
    }

    public Set<Account> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Account> subscribers) {
        this.subscribers = subscribers;
    }

    public boolean addSubscriber(Account subscriber) {
        return subscribers.add(subscriber);
    }

    public boolean deleteSubscriber(Account subscriber) {
        return subscribers.remove(subscriber);
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Account getAccount() {
        return account;
    }

    public Long getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getText() {
        return text;
    }
}