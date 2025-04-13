package gr.evansp.momento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gr.evansp.momento.AbstractUnitTest;
import gr.evansp.momento.beans.JwtTokenInfo;
import gr.evansp.momento.exception.LogicException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestJwtService extends AbstractUnitTest {

	@Autowired JwtService service;

	/**
	 * Test for {@link JwtService#extractUserProfileInfo(String)}
	 */
	@Test
	public void testExtractUserProfileInfo_nullToken() {
		assertThrows(LogicException.class, () -> service.extractUserProfileInfo(null));
	}

	/**
	 * Test for {@link JwtService#extractUserProfileInfo(String)}
	 */
	@Test
	public void testExtractUserProfileInfo_emptyToken() {
		assertThrows(LogicException.class, () -> service.extractUserProfileInfo(""));
	}

	/**
	 * Test for {@link JwtService#extractUserProfileInfo(String)}
	 */
	@Test
	public void testExtractUserProfileInfo_googleToken() {
		JwtTokenInfo info = service.extractUserProfileInfo(VALID_GOOGLE_TOKEN);

		assertEquals("Βαγγέλης", info.firstName());
		assertEquals("Πουλάκης", info.lastName());
		assertEquals("112834076533832138143", info.authenticationProviderId());
		assertEquals("evanspoulaki@gmail.com", info.email());
		assertEquals(
				"https://lh3.googleusercontent.com/a/ACg8ocItjtnWNX4BhkKLDvOV9ChIYx5_r_UzSgftgwIp6TmyZrloVJM=s96-c",
				info.profilePictureUrl());
	}

	/**
	 * Test for {@link JwtService#extractUserProfileInfo(String)}
	 */
	@Test
	public void testExtractUserProfileInfo_keycloakToken() {
		JwtTokenInfo info = service.extractUserProfileInfo(VALID_KEYCLOAK_TOKEN);

		assertEquals("John", info.firstName());
		assertEquals("Doe", info.lastName());
		assertEquals("f:1234567890:abcdef1234567890", info.authenticationProviderId());
		assertEquals("john.doe@example.com", info.email());
		assertNull(info.profilePictureUrl());
	}

	/**
	 * Test for {@link JwtService#extractUserProfileInfo(String)}
	 */
	@Test
	public void testExtractUserProfileInfo_facebookToken() {
		JwtTokenInfo info = service.extractUserProfileInfo(VALID_FACEBOOK_TOKEN);

		assertEquals("Jane", info.firstName());
		assertEquals("Smith", info.lastName());
		assertEquals("123456789012345", info.authenticationProviderId());
		assertEquals("jane.smith@example.com", info.email());
		assertEquals("https://graph.facebook.com/123456789012345/picture", info.profilePictureUrl());
	}


	/**
	 * Test for {@link JwtService#extractUserProfileInfo(String)}
	 */
	@Test
	public void testExtractUserProfileInfo_microsoftToken() {
		JwtTokenInfo info = service.extractUserProfileInfo(VALID_MICROSOFT_TOKEN);

		assertEquals("Alice", info.firstName());
		assertEquals("Johnson", info.lastName());
		assertEquals("P5QJ_qxmf_BxFMSBiLI4ZA8be5dehgbDVo_TrZ8kaEI", info.authenticationProviderId());
		assertEquals("alice.johnson@contoso.com", info.email());
		assertNull(info.profilePictureUrl());
	}

	/**
	 * Test for {@link JwtService#extractUserProfileInfo(String)}
	 */
	@Test
	public void testExtractUserProfileInfo_linkedInToken() {
		JwtTokenInfo info = service.extractUserProfileInfo(VALID_LINKED_IN_TOKEN);

		assertEquals("Jane", info.firstName());
		assertEquals("Doe", info.lastName());
		assertEquals("123456", info.authenticationProviderId());
		assertEquals("jane.doe@linkedin-user.com", info.email());
		assertEquals("https://media.licdn.com/dms/image/C1234567890AB/avatar-profile-picture", info.profilePictureUrl());
	}
}
