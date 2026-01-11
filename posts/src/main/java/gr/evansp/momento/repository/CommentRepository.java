package gr.evansp.momento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gr.evansp.momento.model.Comment;

import java.util.UUID;

/**
 * {@link Repository} for {@link Comment}.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
