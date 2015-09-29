package tales;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    @OneToMany(mappedBy = "account")
    private Set<Tale> tales = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    public Set<TaleLink> taleLinks = new HashSet<>();

    public Account(String name, String password) {
        this.username = name;
        this.password = password;
    }

    Account() { // jpa only
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