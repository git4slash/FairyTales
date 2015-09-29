package tales;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TaleLinkRepository extends JpaRepository<TaleLink, Long> {
    Collection<Tale> findByAccountUsername(String username);
}
