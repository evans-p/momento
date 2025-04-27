package gr.evansp.momento.service;

import gr.evansp.momento.annotation.ValidPage;
import gr.evansp.momento.annotation.ValidPaging;
import gr.evansp.momento.annotation.ValidUserId;
import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import java.util.List;
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
  UserProfile getUser(@ValidUserId String userId);

  /**
   * Fetches the currently logged-in user. Said user is identified by
   * the JWT token provided. Throws an exception if he is non found.
   *
   * @param jwtToken jwtToken
   * @return {@link UserProfile}
   */
  UserProfile getLoggedInUser(String jwtToken);

  /**
   * Gets the {@link List} of users that the current user follows.
   * @param userId userId
   * @param page page
   * @param pageSize pageSize
   * @return {@link List}
   */
  List<UserFollow> getFollows(
      @ValidUserId String userId, @ValidPage int page, @ValidPaging int pageSize);
}
