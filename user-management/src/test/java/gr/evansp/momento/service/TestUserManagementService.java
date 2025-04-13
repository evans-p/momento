package gr.evansp.momento.service;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.exception.LogicException;
import gr.evansp.momento.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static gr.evansp.momento.constant.ExceptionConstants.INVALID_USER_ID;
import static gr.evansp.momento.constant.ExceptionConstants.USER_ALREADY_REGISTERED;
import static gr.evansp.momento.constant.ExceptionConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link UserManagementService}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestUserManagementService extends AbstractIntegrationTest {

	@Autowired
	UserManagementService service;

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
		assertEquals(0L, profile.getFollowedCount());
		assertEquals(0L, profile.getFollowersCount());
	}

	/**
	 * Test for {@link UserManagementService#register(String)}.
	 */
	@Test
	public void testRegister_userAlreadyRegistered() {
		service.register(VALID_GOOGLE_TOKEN);
		LogicException exception = assertThrows(LogicException.class, () -> service.register(VALID_GOOGLE_TOKEN));
		assertEquals(USER_ALREADY_REGISTERED, exception.getMessage());
	}

	/**
	 * Test for {@link UserManagementService#register(String)}.
	 */
	@Test
	public void testGetUser_nullUserId() {
		LogicException e = assertThrows(LogicException.class, () -> service.getUser(null));
		assertEquals(INVALID_USER_ID, e.getMessage());
	}

	/**
	 * Test for {@link UserManagementService#register(String)}.
	 */
	@Test
	public void testGetUser_emptyUserId() {
		LogicException e = assertThrows(LogicException.class, () -> service.getUser(""));
		assertEquals(INVALID_USER_ID, e.getMessage());
	}

	/**
	 * Test for {@link UserManagementService#register(String)}.
	 */
	@Test
	public void testGetUser_blankUserId() {
		LogicException e = assertThrows(LogicException.class, () -> service.getUser("   "));
		assertEquals(INVALID_USER_ID, e.getMessage());
	}

	/**
	 * Test for {@link UserManagementService#register(String)}.
	 */
	@Test
	public void testGetUser_invalidToken() {
		LogicException e = assertThrows(LogicException.class, () -> service.getUser("GameChanger"));
		assertEquals(INVALID_USER_ID, e.getMessage());
	}

	/**
	 * Test for {@link UserManagementService#register(String)}.
	 */
	@Test
	public void testGetUser_userNotFound() {
		LogicException e = assertThrows(LogicException.class, () -> service.getUser(UUID.randomUUID().toString()));
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
		assertEquals(profile.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), fetchProfile.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
		assertEquals(profile.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS), fetchProfile.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
		assertEquals(profile.getFollowersCount(), fetchProfile.getFollowersCount());
		assertEquals(profile.getFollowedCount(), fetchProfile.getFollowedCount());
	}
}