package gr.evansp.momento.repository;

import java.util.UUID;

import gr.evansp.momento.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

}
