package gr.evansp.momento.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Integration tests for {@link UserProfileRepository}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestUserProfileRepository extends AbstractIntegrationTest {

  @Autowired UserProfileRepository repository;

  /**
   * Test for {@link UserProfileRepository#save(Object)}
   */
  @Test
  public void testStore() {
    UserProfile userProfile = getSampleUserProfile();
    UserProfile profile = repository.save(userProfile);

    assertNotNull(profile.getId());
    assertEquals(userProfile.getFirstName(), profile.getFirstName());
    assertEquals(userProfile.getEmail(), profile.getEmail());
    assertEquals(userProfile.getLastName(), profile.getLastName());
    assertEquals(userProfile.getAuthenticationProviderId(), profile.getAuthenticationProviderId());
  }


  /**
   * Test for {@link UserProfileRepository#save(Object)}
   */
  @Test
  public void testFindByAuthenticationProviderId() {
    UserProfile userProfile = getSampleUserProfile();
    UserProfile profile = repository.save(userProfile);

    assertTrue(repository.findByAuthenticationProviderId(profile.getAuthenticationProviderId()).isPresent());
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
