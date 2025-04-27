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
   * Registers new user. Requires a jwt token as input
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
   *
   * @param userId
   * 		userId
   * @return {@link UserProfile}
   */
  UserProfile getUser(@ValidUserId String userId);

  /**
   * Fetches the currently logged-in user. Said user is identified by
   * the JWT token provided. Throw an exception if he is non found.
   *
   * @param jwtToken
   * 		jwtToken
   * @return {@link UserProfile}
   */
  UserProfile getLoggedInUser(String jwtToken);

  /**
   * Gets the {@link List} of users that the given user follows.
   *
   * @param userId
   * 		userId
   * @param page
   * 		page
   * @param pageSize
   * 		pageSize
   * @return {@link List}
   */
  List<UserFollow> getFollows(
      @ValidUserId String userId, @ValidPage int page, @ValidPaging int pageSize);

  /**
   * Gets the {@link List} of users that follow the given user.
   *
   * @param userId
   * 		userId
   * @param page
   * 		page
   * @param pageSize
   * 		pageSize
   * @return {@link List}
   */
  List<UserFollow> getFollowedBy(
      @ValidUserId String userId, @ValidPage int page, @ValidPaging int pageSize);

  /**
   * The currently loggedIn user follows the given user.
   * @param jwtToken jwtToken
   * @param userId userId
   */
  UserFollow follow(String jwtToken, @ValidUserId String userId);

  /**
   * The currently loggedIn user unfollows the given user.
   * @param jwtToken jwtToken
   * @param userId userId
   */
  void unfollow(String jwtToken, @ValidUserId String userId);
}
