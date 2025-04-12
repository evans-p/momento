package gr.evansp.momento.repository;

import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UUID> {

  Page<UserFollow> findByFollower(UserProfile follower, Pageable pageable);

  Page<UserFollow> findByFollowed(UserProfile followed, Pageable pageable);

  Optional<UserFollow> findByFollowerAndFollowed(UserProfile follower, UserProfile followed);
}
