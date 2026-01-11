package gr.evansp.momento.repository;

import gr.evansp.momento.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * {@link Repository} for {@link Post}.
 */
public interface PostRepository extends JpaRepository<Post, UUID> {
}
