package gr.evansp.momento.repository;


import java.util.UUID;

import org.springframework.data.domain.Pageable;
import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UUID> {

	Page<UserFollow> findByFollower(UserProfile follower, Pageable pageable);

	Page<UserFollow> findByFollowed(UserProfile followed, Pageable pageable);
}
