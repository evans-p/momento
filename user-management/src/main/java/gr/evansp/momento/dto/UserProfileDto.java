package gr.evansp.momento.dto;

import gr.evansp.momento.model.UserProfile;

/**
 * Data transfer object for {@link UserProfile}.
 *
 * @param firstName
 * 		firstName
 * @param lastName
 * 		lastName
 * @param profilePictureUrl
 * 		profilePictureUrl
 */
public record UserProfileDto(
    String id,
    String firstName,
    String lastName,
    String profilePictureUrl,
    Long followsCount,
    Long followedCount) {

  public static UserProfileDto of(UserProfile profile) {
    return new UserProfileDto(
        profile.getId().toString(),
        profile.getFirstName(),
        profile.getLastName(),
        profile.getProfilePictureUrl(),
        profile.getFollowsCount(),
        profile.getFollowedCount());
  }
}
