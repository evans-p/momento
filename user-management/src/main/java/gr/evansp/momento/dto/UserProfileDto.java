package gr.evansp.momento.dto;

import gr.evansp.momento.model.UserProfile;

/**
 * Data transfer object for {@link UserProfile}.
 *
 * @param userId
 * 		userId
 * @param firstName
 * 		firstName
 * @param lastName
 * 		lastName
 * @param profilePictureUrl
 * 		profilePictureUrl
 * @param followsCount
 * 		followsCount
 * @param followedByCount
 * 		followedByCount
 */
public record UserProfileDto(
    String userId,
    String firstName,
    String lastName,
    String profilePictureUrl,
    Long followsCount,
    Long followedByCount) {

  public static UserProfileDto of(UserProfile profile) {
    return new UserProfileDto(
        profile.getId().toString(),
        profile.getFirstName(),
        profile.getLastName(),
        profile.getProfilePictureUrl(),
        profile.getFollowsCount(),
        profile.getFollowedByCount());
  }
}
