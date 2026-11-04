package gr.evansp.momento.service;

import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.dto.UserFollowDto;
import gr.evansp.momento.dto.UserProfileDto;
import gr.evansp.momento.model.UserFollow;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Implementation of {@link UserService}. */
@Service
public class UserServiceImpl implements UserService {

  @Autowired UserManagementService userManagementService;

  @Override
  public UserProfileDto register(String jwtToken) {
    return UserProfileDto.of(userManagementService.register(jwtToken));
  }

  @Override
  public UserProfileDto getUser(String userId) {
    return UserProfileDto.of(userManagementService.getUser(userId));
  }

  @Override
  public UserProfileDto getLoggedInUser(String jwtToken) {
    return UserProfileDto.of(userManagementService.getLoggedInUser(jwtToken));
  }

  @Override
  public List<UserProfileDto> getLoggedInUserFollows(String jwtToken, int page, int pageSize) {

    return userManagementService.getLoggedInUserFollows(jwtToken, page, pageSize).stream()
        .map(UserFollow::getFollowed)
        .map(UserProfileDto::of)
        .toList();
  }

  @Override
  public List<UserProfileDto> getLoggedInUserFollowed(String jwtToken, int page, int pageSize) {
    return userManagementService.getLoggedInUserFollowed(jwtToken, page, pageSize).stream()
        .map(UserFollow::getFollows)
        .map(UserProfileDto::of)
        .toList();
  }

  @Override
  public List<UserProfileDto> getFollows(String userId, int page, int pageSize) {
    return userManagementService.getFollows(userId, page, pageSize).stream()
        .map(UserFollow::getFollowed)
        .map(UserProfileDto::of)
        .toList();
  }

  @Override
  public List<UserProfileDto> getFollowed(String userId, int page, int pageSize) {
    return userManagementService.getFollowed(userId, page, pageSize).stream()
        .map(UserFollow::getFollows)
        .map(UserProfileDto::of)
        .toList();
  }

  @Override
  public UserFollowDto follow(String jwtToken, String userId) {
    return UserFollowDto.of(userManagementService.follow(jwtToken, userId));
  }

  @Override
  public void unfollow(String jwtToken, String userId) {
    userManagementService.unfollow(jwtToken, userId);
  }

  @Override
  public UserProfileDto updateProfile(String jwtToken, UpdateUserProfileDto profileDto) {
    return UserProfileDto.of(userManagementService.updateProfile(jwtToken, profileDto));
  }
}
