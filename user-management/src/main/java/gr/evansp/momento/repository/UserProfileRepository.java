package gr.evansp.momento.repository;

import gr.evansp.momento.model.UserProfile;

import java.util.Optional;
import java.util.UUID;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

	Optional<UserProfile> findByAuthenticationProviderId(String authenticationProviderId);
}
