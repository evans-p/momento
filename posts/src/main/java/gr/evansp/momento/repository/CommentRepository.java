package gr.evansp.momento.repository;

import gr.evansp.momento.model.Comment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link Repository} for {@link Comment}.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {}
