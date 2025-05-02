package gr.evansp.momento.service;

import static gr.evansp.momento.constant.ExceptionConstants.USER_ALREADY_REGISTERED;
import static gr.evansp.momento.constant.ExceptionConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.exception.LogicException;
import gr.evansp.momento.exception.ResourceNotFoundException;
import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import gr.evansp.momento.repository.UserFollowRepository;
import gr.evansp.momento.repository.UserProfileRepository;
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
@SuppressWarnings("OptionalGetWithoutIsPresent")
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
   * {@link UserProfileRepository}.
   */
  @Autowired UserProfileRepository userProfileRepository;

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
    ResourceNotFoundException e =
        assertThrows(
            ResourceNotFoundException.class, () -> service.getUser(UUID.randomUUID().toString()));
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
    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class, () -> service.getLoggedInUser(VALID_GOOGLE_TOKEN));
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
    ResourceNotFoundException e =
        assertThrows(
            ResourceNotFoundException.class,
            () -> service.getFollows(UUID.randomUUID().toString(), 1, 1));
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

    List<UserFollow> profile1Follows = service.getFollows(profile1.getId().toString(), 0, 30);
    List<UserFollow> profile2Follows = service.getFollows(profile2.getId().toString(), 0, 30);

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
    ResourceNotFoundException e =
        assertThrows(
            ResourceNotFoundException.class,
            () -> service.getFollowedBy(UUID.randomUUID().toString(), 1, 1));
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

    List<UserFollow> profile1FollowedBy = service.getFollowedBy(profile1.getId().toString(), 0, 30);
    List<UserFollow> profile2FollowedBy = service.getFollowedBy(profile2.getId().toString(), 0, 30);

    assertEquals(1, profile1FollowedBy.size());
    assertEquals(1, profile2FollowedBy.size());

    assertEquals(profile2, profile1FollowedBy.getFirst().getFollows());
    assertEquals(profile1, profile1FollowedBy.getFirst().getFollowedBy());

    assertEquals(profile1, profile2FollowedBy.getFirst().getFollows());
    assertEquals(profile2, profile2FollowedBy.getFirst().getFollowedBy());
  }

  /**
   * Test for {@link UserManagementService#follow(String, String)}.
   */
  @Test
  public void testFollow_loggedInUserNotFound() {
    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> service.follow(VALID_GOOGLE_TOKEN, UUID.randomUUID().toString()));
    assertEquals(USER_NOT_FOUND, exception.getMessage());
  }

  /**
   * Test for {@link UserManagementService#follow(String, String)}.
   */
  @Test
  public void testFollow_invalidFollowedUserId() {
    service.register(VALID_GOOGLE_TOKEN);
    ConstraintViolationException exception =
        assertThrows(
            ConstraintViolationException.class, () -> service.follow(VALID_GOOGLE_TOKEN, "tinMan"));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.user.id"),
        exception.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#follow(String, String)}.
   */
  @Test
  public void testFollow_followAlreadyExists() {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = new UserFollow();
    follow.setFollows(profile1);
    follow.setFollowedBy(profile2);

    userFollowRepository.save(follow);

    profile1.setFollowsCount(profile1.getFollowsCount() + 1);
    profile2.setFollowedByCount(profile2.getFollowedByCount() + 1);

    profile1 = userProfileRepository.save(profile1);
    profile2 = userProfileRepository.save(profile2);

    assertEquals(0, profile1.getFollowedByCount());
    assertEquals(1, profile1.getFollowsCount());
    assertEquals(1, profile2.getFollowedByCount());
    assertEquals(0, profile2.getFollowsCount());

    service.follow(VALID_GOOGLE_TOKEN, profile2.getId().toString());

    profile1 = userProfileRepository.findById(profile1.getId()).get();
    profile2 = userProfileRepository.findById(profile2.getId()).get();

    assertEquals(0, profile1.getFollowedByCount());
    assertEquals(1, profile1.getFollowsCount());
    assertEquals(1, profile2.getFollowedByCount());
    assertEquals(0, profile2.getFollowsCount());
    assertEquals(1, userFollowRepository.findAll().size());
  }

  /**
   * Test for {@link UserManagementService#follow(String, String)}.
   */
  @Test
  public void testFollow_ok() {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    assertEquals(0, profile1.getFollowedByCount());
    assertEquals(0, profile1.getFollowsCount());
    assertEquals(0, profile2.getFollowedByCount());
    assertEquals(0, profile2.getFollowsCount());

    UserFollow follow = service.follow(VALID_GOOGLE_TOKEN, profile2.getId().toString());

    assertEquals(profile1, follow.getFollows());
    assertEquals(profile2, follow.getFollowedBy());

    profile1 = userProfileRepository.findById(profile1.getId()).get();
    profile2 = userProfileRepository.findById(profile2.getId()).get();

    assertEquals(0, profile1.getFollowedByCount());
    assertEquals(1, profile1.getFollowsCount());
    assertEquals(1, profile2.getFollowedByCount());
    assertEquals(0, profile2.getFollowsCount());
    assertEquals(1, userFollowRepository.findAll().size());
  }

  /**
   * Test for {@link UserManagementService#unfollow(String, String)}.
   */
  @Test
  public void testUnfollow_loggedInUserNotFound() {
    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> service.unfollow(VALID_GOOGLE_TOKEN, UUID.randomUUID().toString()));
    assertEquals(USER_NOT_FOUND, exception.getMessage());
  }

  /**
   * Test for {@link UserManagementService#unfollow(String, String)}.
   */
  @Test
  public void testUnfollow_invalidFollowedUserId() {
    service.register(VALID_GOOGLE_TOKEN);
    ConstraintViolationException exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> service.unfollow(VALID_GOOGLE_TOKEN, "tinMan"));
    assertEquals(
        VALIDATION_MESSAGES.getString("invalid.user.id"),
        exception.getConstraintViolations().iterator().next().getMessage());
  }

  /**
   * Test for {@link UserManagementService#unfollow(String, String)}.
   */
  @Test
  public void testUnfollow_followDoesNotExist() {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    assertEquals(0, profile1.getFollowedByCount());
    assertEquals(0, profile1.getFollowsCount());
    assertEquals(0, profile2.getFollowedByCount());
    assertEquals(0, profile2.getFollowsCount());
    assertEquals(0, userFollowRepository.findAll().size());

    service.unfollow(VALID_GOOGLE_TOKEN, profile2.getId().toString());

    profile1 = userProfileRepository.findById(profile1.getId()).get();
    profile2 = userProfileRepository.findById(profile2.getId()).get();

    assertEquals(0, profile1.getFollowedByCount());
    assertEquals(0, profile1.getFollowsCount());
    assertEquals(0, profile2.getFollowedByCount());
    assertEquals(0, profile2.getFollowsCount());
    assertEquals(0, userFollowRepository.findAll().size());
  }

  /**
   * Test for {@link UserManagementService#unfollow(String, String)}.
   */
  @Test
  public void testUnfollow_ok() {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = service.follow(VALID_GOOGLE_TOKEN, profile2.getId().toString());

    assertEquals(profile1, follow.getFollows());
    assertEquals(profile2, follow.getFollowedBy());

    profile1 = userProfileRepository.findById(profile1.getId()).get();
    profile2 = userProfileRepository.findById(profile2.getId()).get();

    assertEquals(0, profile1.getFollowedByCount());
    assertEquals(1, profile1.getFollowsCount());
    assertEquals(1, profile2.getFollowedByCount());
    assertEquals(0, profile2.getFollowsCount());
    assertEquals(1, userFollowRepository.findAll().size());

    service.unfollow(VALID_GOOGLE_TOKEN, profile2.getId().toString());

    profile1 = userProfileRepository.findById(profile1.getId()).get();
    profile2 = userProfileRepository.findById(profile2.getId()).get();

    assertEquals(0, profile1.getFollowedByCount());
    assertEquals(0, profile1.getFollowsCount());
    assertEquals(0, profile2.getFollowedByCount());
    assertEquals(0, profile2.getFollowsCount());
    assertEquals(0, userFollowRepository.findAll().size());
  }

  /**
   * Test for {@link UserManagementService#updateProfile(String, UpdateUserProfileDto)} .
   */
  @Test
  public void testUpdateProfile_ok() {
    UserProfile profile = service.register(VALID_GOOGLE_TOKEN);

    UpdateUserProfileDto profileDto =
        new UpdateUserProfileDto("Kendrick", "Lamar", profile.getProfilePictureUrl());

    UserProfile fetchProfile = service.updateProfile(VALID_GOOGLE_TOKEN, profileDto);

    assertEquals(profileDto.firstName(), fetchProfile.getFirstName());
    assertEquals(profileDto.lastName(), fetchProfile.getLastName());
  }

  /**
   * Test for {@link UserManagementService#getLoggedInUserFollows(String, int, int)}.
   */
  @Test
  public void testGetLoggedInUserFollows_ok() {
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

    List<UserFollow> follows = service.getLoggedInUserFollows(VALID_GOOGLE_TOKEN, 0, 30);

    assertEquals(1, follows.size());
    assertEquals(profile1, follows.getFirst().getFollows());
    assertEquals(profile2, follows.getFirst().getFollowedBy());
  }

  /**
   * Test for {@link UserManagementService#getLoggedInUserFollows(String, int, int)}.
   */
  @Test
  public void testGetLoggedInUserFollowedBy_ok() {
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

    List<UserFollow> follows = service.getLoggedInUserFollowedBy(VALID_GOOGLE_TOKEN, 0, 30);

    assertEquals(1, follows.size());
    assertEquals(profile2, follows.getFirst().getFollows());
    assertEquals(profile1, follows.getFirst().getFollowedBy());
  }
}
