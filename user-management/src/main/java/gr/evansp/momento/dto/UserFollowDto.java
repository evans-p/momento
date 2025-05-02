package gr.evansp.momento.dto;

import gr.evansp.momento.model.UserFollow;

/**
 * Data transfer object for {@link UserFollow}.
 * @param follows {@link UserProfileDto}.
 * @param followedBy {@link UserProfileDto}.
 */
public record UserFollowDto(UserProfileDto follows, UserProfileDto followedBy) {

  public static UserFollowDto of(UserFollow userFollow) {
    UserProfileDto followsDto = UserProfileDto.of(userFollow.getFollows());
    UserProfileDto followedByDto = UserProfileDto.of(userFollow.getFollowedBy());

    return new UserFollowDto(followsDto, followedByDto);
  }
}
