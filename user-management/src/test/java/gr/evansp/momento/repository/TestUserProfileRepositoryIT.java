package gr.evansp.momento.repository;


import java.util.Optional;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for {@link UserProfileRepository}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestUserProfileRepositoryIT extends AbstractIntegrationTest {

  @Autowired
  UserProfileRepository repository;

  @Autowired
  UserFollowRepository followRepository;

  @Test
  public void testStore() {
    UserProfile userProfile = getSampleUserProfile();
    UserProfile profile = repository.save(userProfile);

    assertNotNull(profile.getId());
    assertEquals(userProfile.getFirstName(), profile.getFirstName());
    assertEquals(userProfile.getEmail(), profile.getEmail());
    assertEquals(userProfile.getLastName(), profile.getLastName());
    assertEquals(userProfile.getAuthenticationProviderId(), profile.getAuthenticationProviderId());

    System.out.println(profile);
  }

  private UserProfile getSampleUserProfile() {
    UserProfile profile = new UserProfile();
    profile.setEmail("test@test.com");
    profile.setFirstName("Evans");
    profile.setLastName("Poulakis");
    profile.setAuthenticationProviderId("authenticationProviderId");

    return profile;
  }
}
