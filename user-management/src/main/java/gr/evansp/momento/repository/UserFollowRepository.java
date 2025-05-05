package gr.evansp.momento.repository;

import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link Repository} for {@link UserFollow}.
 */
@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UUID> {

  /**
   * Gets all the {@link UserFollow}, meaning all the users
   * that a {@link UserProfile} follows.
   *
   * @param follows
   *        {@link UserProfile}
   * @param pageable
   *        {@link Pageable}.
   * @return {@link Page}
   */
  Page<UserFollow> findByFollows(UserProfile follows, Pageable pageable);

  /**
   * Gets all the {@link UserFollow}, meaning all the {@link UserProfile}
   * are followed by the user.
   *
   * @param followed
   *        {@link UserProfile}
   * @param pageable
   *        {@link Pageable}.
   * @return {@link Page}
   */
  Page<UserFollow> findByFollowed(UserProfile followed, Pageable pageable);

  /**
   * Finds the {@link UserFollow} relation between two {@link UserProfile}.
   *
   * @param follows
   *        {@link UserProfile}
   * @param followed
   *        {@link UserProfile}
   * @return {@link UserFollow}
   */
  Optional<UserFollow> findByFollowsAndFollowed(UserProfile follows, UserProfile followed);
}
