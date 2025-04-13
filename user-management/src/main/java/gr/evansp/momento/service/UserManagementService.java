package gr.evansp.momento.service;

import gr.evansp.momento.model.UserProfile;
import org.springframework.stereotype.Service;

/**
 * {@link Service} for managing users
 */

public interface UserManagementService {
	/**
	 * Registers new user. requires a jwt token as input
	 * to get the initial profile data from it, like
	 * first name, last name, email, etc. First validates
	 * the given token. Then checks that the token's auth
	 * provider does not already exist, and then creates
	 * and stores a new user.
	 *
	 * @param jwtToken
	 * 		jwtToken
	 */
	UserProfile register(String jwtToken);


	/**
	 * Gets {@link UserProfile} by user ID. Throws Exception
	 * if the userId is not valid, meaning does not follow
	 * the UUID pattern, or the user is not found.
	 * @param userId userId
	 * @return {@link UserProfile}
	 */
	UserProfile getUser(String userId);
}
