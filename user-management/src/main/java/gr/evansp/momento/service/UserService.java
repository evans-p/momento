package gr.evansp.momento.service;

import gr.evansp.momento.controller.UserManagementController;
import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.dto.UserFollowDto;
import gr.evansp.momento.dto.UserProfileDto;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Intermediate {@link Service} Between {@link UserManagementService} and {@link
 * UserManagementController}.
 *
 * <p>Delegates the business logic to {@link UserManagementService} and maps the resulting domain
 * models to DTOs, so that the controller layer never deals with persistence entities.
 */
public interface UserService {

  /**
   * Registers a new user using the data (first name, last name, email, etc.) contained in the given
   * JWT token.
   *
   * @param jwtToken the JWT token of the user to register
   * @return the {@link UserProfileDto} of the newly registered user
   */
  UserProfileDto register(String jwtToken);

  /**
   * Gets the profile of the user with the given ID.
   *
   * @param userId the ID of the user, which must follow the UUID pattern
   * @return the {@link UserProfileDto} of the requested user
   */
  UserProfileDto getUser(String userId);

  /**
   * Gets the profile of the currently logged-in user, as identified by the given JWT token.
   *
   * @param jwtToken the JWT token of the logged-in user
   * @return the {@link UserProfileDto} of the logged-in user
   */
  UserProfileDto getLoggedInUser(String jwtToken);

  /**
   * Gets a page of the users that the currently logged-in user follows.
   *
   * @param jwtToken the JWT token of the logged-in user
   * @param page the zero-based page index
   * @param pageSize the maximum number of users to return per page
   * @return a {@link List} of {@link UserProfileDto} of the followed users
   */
  List<UserProfileDto> getLoggedInUserFollows(String jwtToken, int page, int pageSize);

  /**
   * Gets a page of the users that follow the currently logged-in user (their followers).
   *
   * @param jwtToken the JWT token of the logged-in user
   * @param page the zero-based page index
   * @param pageSize the maximum number of users to return per page
   * @return a {@link List} of {@link UserProfileDto} of the followers
   */
  List<UserProfileDto> getLoggedInUserFollowed(String jwtToken, int page, int pageSize);

  /**
   * Gets a page of the users that the given user follows.
   *
   * @param userId the ID of the user, which must follow the UUID pattern
   * @param page the zero-based page index
   * @param pageSize the maximum number of users to return per page
   * @return a {@link List} of {@link UserProfileDto} of the followed users
   */
  List<UserProfileDto> getFollows(String userId, int page, int pageSize);

  /**
   * Gets a page of the users that follow the given user (their followers).
   *
   * @param userId the ID of the user, which must follow the UUID pattern
   * @param page the zero-based page index
   * @param pageSize the maximum number of users to return per page
   * @return a {@link List} of {@link UserProfileDto} of the followers
   */
  List<UserProfileDto> getFollowed(String userId, int page, int pageSize);

  /**
   * Makes the currently logged-in user follow the given user.
   *
   * @param jwtToken the JWT token of the logged-in user
   * @param userId the ID of the user to follow, which must follow the UUID pattern
   * @return a {@link UserFollowDto} describing the created follow relationship
   */
  UserFollowDto follow(String jwtToken, String userId);

  /**
   * Makes the currently logged-in user unfollow the given user.
   *
   * @param jwtToken the JWT token of the logged-in user
   * @param userId the ID of the user to unfollow, which must follow the UUID pattern
   */
  void unfollow(String jwtToken, String userId);

  /**
   * Updates the profile of the currently logged-in user.
   *
   * @param jwtToken the JWT token of the logged-in user
   * @param profileDto the {@link UpdateUserProfileDto} holding the new profile data
   * @return the updated {@link UserProfileDto}
   */
  UserProfileDto updateProfile(String jwtToken, UpdateUserProfileDto profileDto);
}
