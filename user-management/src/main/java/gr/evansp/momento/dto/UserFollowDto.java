package gr.evansp.momento.dto;

import gr.evansp.momento.model.UserFollow;

/**
 * Data transfer object for {@link UserFollow}.
 * @param follows {@link UserProfileDto}.
 * @param followed {@link UserProfileDto}.
 */
public record UserFollowDto(UserProfileDto follows, UserProfileDto followed) {

  public static UserFollowDto of(UserFollow userFollow) {
    UserProfileDto followsDto = UserProfileDto.of(userFollow.getFollows());
    UserProfileDto followedDto = UserProfileDto.of(userFollow.getFollowed());

    return new UserFollowDto(followsDto, followedDto);
  }
}
