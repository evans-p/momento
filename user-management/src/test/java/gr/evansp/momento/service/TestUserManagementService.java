package gr.evansp.momento.service;

import static gr.evansp.momento.constant.ExceptionConstants.USER_ALREADY_REGISTERED;
import static gr.evansp.momento.constant.ExceptionConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.exception.LogicException;
import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import gr.evansp.momento.repository.UserFollowRepository;
import jakarta.validation.ConstraintViolationException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Integration tests for {@link UserManagementService}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestUserManagementService extends AbstractIntegrationTest {

  /**
   * {@link UserManagementService}.
   */
  @Autowired UserManagementService service;

  /**
   * {@link UserFollowRepository}.
   */
  @Autowired UserFollowRepository userFollowRepository;

  /**
   * Test for {@link UserManagementService#register(String)}.
   */
  @Test
  public void testRegister_ok() {
    UserProfile profile = service.register(VALID_GOOGLE_TOKEN);

    assertNotNull(profile.getId());
    assertNotNull(profile.getFirstName());
    assertNotNull(profile.getEmail());
    assertNotNull(profile.getLastName());
    assertNotNull(profile.getAuthenticationProviderId());
    assertEquals(0L, profile.getFollowsCount());
    assertEquals(0L, profile.getFollowedByCount());
  }

  /**
   * Test for {@link UserManagementService#register(String)}.
   */
  @Test
  public void testRegister_userAlreadyRegistered() {
    service.register(VALID_GOOGLE_TOKEN);
    LogicException exception =
        assertThrows(LogicException.class, () -> service.register(VALID_GOOGLE_TOKEN));
    assertEquals(USER_ALREADY_REGISTERED, exception.getMessage());
  }

  /**
   * Test for {@link UserManagementService#register(String)}.
   */
  @Test
  public void testGetUser_nullUserId() {
    ConstraintViolationException e =
        assertThrows(ConstraintViolationException.class, () -> service.getUser(null));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.user.id"),
        e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#register(String)}.
   */
  @Test
  public void testGetUser_emptyUserId() {
    ConstraintViolationException e =
        assertThrows(ConstraintViolationException.class, () -> service.getUser(""));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.user.id"),
        e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#register(String)}.
   */
  @Test
  public void testGetUser_blankUserId() {
    ConstraintViolationException e =
        assertThrows(ConstraintViolationException.class, () -> service.getUser("   "));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.user.id"),
        e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#register(String)}.
   */
  @Test
  public void testGetUser_invalidToken() {
    ConstraintViolationException e =
        assertThrows(ConstraintViolationException.class, () -> service.getUser("GameChanger"));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.user.id"),
        e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#register(String)}.
   */
  @Test
  public void testGetUser_userNotFound() {
    LogicException e =
        assertThrows(LogicException.class, () -> service.getUser(UUID.randomUUID().toString()));
    assertEquals(USER_NOT_FOUND, e.getMessage());
  }

  /**
   * Test for {@link UserManagementService#register(String)}.
   */
  @Test
  public void testGetUser_ok() {
    UserProfile profile = service.register(VALID_GOOGLE_TOKEN);
    UserProfile fetchProfile = service.getUser(profile.getId().toString());

    assertEquals(profile.getId(), fetchProfile.getId());
    assertEquals(profile.getFirstName(), fetchProfile.getFirstName());
    assertEquals(profile.getLastName(), fetchProfile.getLastName());
    assertEquals(profile.getEmail(), fetchProfile.getEmail());
    assertEquals(profile.getProfilePictureUrl(), fetchProfile.getProfilePictureUrl());
    assertEquals(
        profile.getCreatedAt().truncatedTo(ChronoUnit.MILLIS),
        fetchProfile.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertEquals(
        profile.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),
        fetchProfile.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertEquals(profile.getFollowsCount(), fetchProfile.getFollowsCount());
    assertEquals(profile.getFollowedByCount(), fetchProfile.getFollowedByCount());
  }

  /**
   * Test for {@link UserManagementService#getLoggedInUser(String)}.
   */
  @Test
  public void testGetLoggedInUser_userNotFound() {
    LogicException exception =
        assertThrows(LogicException.class, () -> service.getLoggedInUser(VALID_GOOGLE_TOKEN));
    assertEquals(USER_NOT_FOUND, exception.getMessage());
  }

  /**
   * Test for {@link UserManagementService#getLoggedInUser(String)}.
   */
  @Test
  public void testGetLoggedInUser_ok() {
    UserProfile profile = service.register(VALID_GOOGLE_TOKEN);
    UserProfile fetchProfile = service.getLoggedInUser(VALID_GOOGLE_TOKEN);

    assertEquals(profile.getId(), fetchProfile.getId());
    assertEquals(profile.getFirstName(), fetchProfile.getFirstName());
    assertEquals(profile.getLastName(), fetchProfile.getLastName());
    assertEquals(profile.getEmail(), fetchProfile.getEmail());
    assertEquals(profile.getProfilePictureUrl(), fetchProfile.getProfilePictureUrl());
    assertEquals(
        profile.getCreatedAt().truncatedTo(ChronoUnit.MILLIS),
        fetchProfile.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertEquals(
        profile.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),
        fetchProfile.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertEquals(profile.getFollowsCount(), fetchProfile.getFollowsCount());
    assertEquals(profile.getFollowedByCount(), fetchProfile.getFollowedByCount());
  }

  /**
   * Test for {@link UserManagementService#getFollows(String, int, int)}.
   */
  @Test
  public void testGetFollows_nullUserId() {
    ConstraintViolationException e =
        assertThrows(ConstraintViolationException.class, () -> service.getFollows(null, 1, 1));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.user.id"),
        e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollows(String, int, int)}.
   */
  @Test
  public void testGetFollows_negativePage() {
    ConstraintViolationException e =
        assertThrows(
            ConstraintViolationException.class,
            () -> service.getFollows(UUID.randomUUID().toString(), -1, 1));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.page"),
        e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollows(String, int, int)}.
   */
  @Test
  public void testGetFollows_negativePageSize() {
    ConstraintViolationException e =
        assertThrows(
            ConstraintViolationException.class,
            () -> service.getFollows(UUID.randomUUID().toString(), 1, -1));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.paging"),
        e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollows(String, int, int)}.
   */
  @Test
  public void testGetFollows_zeroPageSize() {
    ConstraintViolationException e =
        assertThrows(
            ConstraintViolationException.class,
            () -> service.getFollows(UUID.randomUUID().toString(), 1, 0));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.paging"),
        e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollows(String, int, int)}.
   */
  @Test
  public void testGetFollows_faultyPagination() {
    ConstraintViolationException e =
        assertThrows(
            ConstraintViolationException.class,
            () -> service.getFollows(UUID.randomUUID().toString(), -1, -1));

    assertEquals(2, e.getConstraintViolations().size());
  }

  /**
   * Test for {@link UserManagementService#getFollows(String, int, int)}.
   */
  @Test
  public void testGetFollows_userNotFound() {
    LogicException e =
        assertThrows(
            LogicException.class, () -> service.getFollows(UUID.randomUUID().toString(), 1, 1));
    assertEquals(USER_NOT_FOUND, e.getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollows(String, int, int)}.
   */
  @Test
  public void testGetFollows_ok() {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = new UserFollow();
    follow.setFollows(profile1);
    follow.setFollowedBy(profile2);

    userFollowRepository.save(follow);

    UserFollow follow2 = new UserFollow();
    follow2.setFollows(profile2);
    follow2.setFollowedBy(profile1);

    userFollowRepository.save(follow2);

    List<UserFollow> profile1Follows = service.getFollows(profile1.getId().toString(), 0, 100);
    List<UserFollow> profile2Follows = service.getFollows(profile2.getId().toString(), 0, 100);

    assertEquals(1, profile1Follows.size());
    assertEquals(1, profile2Follows.size());

    assertEquals(profile1, profile1Follows.getFirst().getFollows());
    assertEquals(profile2, profile1Follows.getFirst().getFollowedBy());

    assertEquals(profile2, profile2Follows.getFirst().getFollows());
    assertEquals(profile1, profile2Follows.getFirst().getFollowedBy());
  }

  /**
   * Test for {@link UserManagementService#getFollowedBy(String, int, int)}.
   */
  @Test
  public void testGetFollowedBy_nullUserId() {
    ConstraintViolationException e =
            assertThrows(ConstraintViolationException.class, () -> service.getFollows(null, 1, 1));
    assertEquals(
            VALIDATION_MESSAGES.getString("invalid.user.id"),
            e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollowedBy(String, int, int)}.
   */
  @Test
  public void testGetFollowedBy_negativePage() {
    ConstraintViolationException e =
            assertThrows(
                    ConstraintViolationException.class,
                    () -> service.getFollowedBy(UUID.randomUUID().toString(), -1, 1));
    assertEquals(
            VALIDATION_MESSAGES.getString("invalid.page"),
            e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollowedBy(String, int, int)}.
   */
  @Test
  public void testGetFollowedBy_negativePageSize() {
    ConstraintViolationException e =
            assertThrows(
                    ConstraintViolationException.class,
                    () -> service.getFollowedBy(UUID.randomUUID().toString(), 1, -1));
    assertEquals(
            VALIDATION_MESSAGES.getString("invalid.paging"),
            e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollowedBy(String, int, int)}.
   */
  @Test
  public void testGetFollowedBy_zeroPageSize() {
    ConstraintViolationException e =
            assertThrows(
                    ConstraintViolationException.class,
                    () -> service.getFollowedBy(UUID.randomUUID().toString(), 1, 0));
    assertEquals(
            VALIDATION_MESSAGES.getString("invalid.paging"),
            e.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollowedBy(String, int, int)}.
   */
  @Test
  public void testGetFollowedBy_faultyPagination() {
    ConstraintViolationException e =
            assertThrows(
                    ConstraintViolationException.class,
                    () -> service.getFollowedBy(UUID.randomUUID().toString(), -1, -1));

    assertEquals(2, e.getConstraintViolations().size());
  }

  /**
   * Test for {@link UserManagementService#getFollowedBy(String, int, int)}.
   */
  @Test
  public void testGetFollowedBy_userNotFound() {
    LogicException e =
            assertThrows(
                    LogicException.class, () -> service.getFollowedBy(UUID.randomUUID().toString(), 1, 1));
    assertEquals(USER_NOT_FOUND, e.getMessage());
  }

  /**
   * Test for {@link UserManagementService#getFollowedBy(String, int, int)}.
   */
  @Test
  public void testGetFollowedBy_ok() {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = new UserFollow();
    follow.setFollows(profile1);
    follow.setFollowedBy(profile2);

    userFollowRepository.save(follow);

    UserFollow follow2 = new UserFollow();
    follow2.setFollows(profile2);
    follow2.setFollowedBy(profile1);

    userFollowRepository.save(follow2);

    List<UserFollow> profile1FollowedBy = service.getFollowedBy(profile1.getId().toString(), 0, 100);
    List<UserFollow> profile2FollowedBy = service.getFollowedBy(profile2.getId().toString(), 0, 100);

    assertEquals(1, profile1FollowedBy.size());
    assertEquals(1, profile2FollowedBy.size());

    assertEquals(profile2, profile1FollowedBy.getFirst().getFollows());
    assertEquals(profile1, profile1FollowedBy.getFirst().getFollowedBy());

    assertEquals(profile1, profile2FollowedBy.getFirst().getFollows());
    assertEquals(profile2, profile2FollowedBy.getFirst().getFollowedBy());
  }
}
