package farytale.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StoryRepository extends JpaRepository<Story, Long> {
    Collection<Story> findByAuthorUsername(String author);
}
