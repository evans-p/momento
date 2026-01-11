package gr.evansp.momento.repository;

import gr.evansp.momento.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * {@link Repository} for {@link Like}.
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
}
