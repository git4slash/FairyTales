package tales;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TaleLink {

    @Id
    private Long id;

    public String uri;

    @JsonIgnore
    @ManyToOne
    private Account account;

    public TaleLink() {
    }

    public TaleLink(Long id, String uri, Account account) {
        this.id = id;
        this.uri = uri;
        this.account = account;
    }
}
