package gr.evansp.momento.service;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.exception.LogicException;
import gr.evansp.momento.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static gr.evansp.momento.constant.ExceptionConstants.USER_ALREADY_REGISTERED;
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
}