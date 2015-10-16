package tales;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    public String password;
    public String username;

    @JsonIgnore
    @ManyToMany(targetEntity = Tale.class)
    private Set<Tale> subscribedTales = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Tale> tales = new HashSet<>();

    public Account(String name, String password) {
        this.username = name;
        this.password = password;
    }

    Account() { // jpa only
    }

    public Set<Tale> getSubscribedTales() {
        return subscribedTales;
    }

    public void setSubscribedTales(Set<Tale> subscribedTales) {
        this.subscribedTales = subscribedTales;
    }

    public boolean unSubscribeFrom(Tale tale) {
        return subscribedTales.remove(tale);
    }

    public boolean subscribeOn(Tale tale) {
        return subscribedTales.add(tale);
    }

    public Set<Tale> getTales() {
        return tales;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}