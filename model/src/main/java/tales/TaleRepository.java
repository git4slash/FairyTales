package tales;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TaleRepository extends JpaRepository<Tale, Long> {
    Collection<Tale> findByAccountUsername(String username);
}
