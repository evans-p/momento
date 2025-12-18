package gr.evansp.momento.service;

import static gr.evansp.momento.constant.ExceptionConstants.*;

import gr.evansp.momento.annotation.ValidPage;
import gr.evansp.momento.annotation.ValidPaging;
import gr.evansp.momento.annotation.ValidUpdateUserProfileDto;
import gr.evansp.momento.annotation.ValidUserId;
import gr.evansp.momento.beans.JwtTokenInfo;
import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.exception.LogicException;
import gr.evansp.momento.exception.ResourceNotFoundException;
import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import gr.evansp.momento.repository.UserFollowRepository;
import gr.evansp.momento.repository.UserProfileRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Implementation of {@link UserManagementService}.
 */
@Slf4j
@Validated
@Service
public class UserManagementServiceImpl implements UserManagementService {

  /**
   * {@link JwtService}.
   */
  @Autowired JwtService jwtService;

  /**
   * {@link UserProfileRepository}.
   */
  @Autowired UserProfileRepository repository;

  /**
   * {@link UserFollowRepository}.
   */
  @Autowired UserFollowRepository userFollowRepository;

  /**
   * {@link FollowProducerService}.
   */
  @Autowired FollowProducerService followProducerService;


  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Override
  public UserProfile register(String jwtToken) {
    JwtTokenInfo tokenInfo = jwtService.extractUserProfileInfo(jwtToken);

    Optional<UserProfile> fetchedProfile =
        repository.findByAuthenticationProviderId(tokenInfo.authenticationProviderId());

    if (fetchedProfile.isPresent()) {
      log.info(
          "register: attempted registration by existing user. Email: {}, id: {}.",
          fetchedProfile.get().getEmail(),
          fetchedProfile.get().getId());
      throw new LogicException(USER_ALREADY_REGISTERED, null);
    }

    fetchedProfile = repository.findByEmail(tokenInfo.email());

    if (fetchedProfile.isPresent()) {
      log.warn("register: valid token with the same mail as existing user: {}.", tokenInfo.email());
      throw new LogicException(EMAIL_ALREADY_REGISTERED, new Object[] {tokenInfo.email()});
    }

    return repository.save(createUserProfileFromJwtTokenInfo(tokenInfo));
  }

  @Override
  public UserProfile getUser(@ValidUserId String userId) {
    return repository
        .findById(UUID.fromString(userId))
        .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, new Object[] {userId}));
  }

  @Override
  public UserProfile getLoggedInUser(String jwtToken) {
    JwtTokenInfo tokenInfo = jwtService.extractUserProfileInfo(jwtToken);

    return repository
        .findByAuthenticationProviderId(tokenInfo.authenticationProviderId())
        .orElseThrow(
            () -> {
              log.warn(
                  "getLoggedInUser: logged in user, not found. email: {}, providerId: {}",
                  tokenInfo.email(),
                  tokenInfo.authenticationProviderId());
              return new ResourceNotFoundException(USER_NOT_FOUND, new Object[] {""});
            });
  }

  @Override
  public List<UserFollow> getLoggedInUserFollows(
      String jwtToken, @ValidPage int page, @ValidPaging int pageSize) {
    UserProfile profile = getLoggedInUser(jwtToken);
    return userFollowRepository.findByFollows(profile, PageRequest.of(page, pageSize)).getContent();
  }

  @Override
  public List<UserFollow> getLoggedInUserFollowed(
      String jwtToken, @ValidPage int page, @ValidPaging int pageSize) {
    UserProfile profile = getLoggedInUser(jwtToken);
    return userFollowRepository
        .findByFollowed(profile, PageRequest.of(page, pageSize))
        .getContent();
  }

  @Override
  public List<UserFollow> getFollows(
      @ValidUserId String userId, @ValidPage int page, @ValidPaging int pageSize) {
    UserProfile profile =
        repository
            .findById(UUID.fromString(userId))
            .orElseThrow(
                () -> new ResourceNotFoundException(USER_NOT_FOUND, new Object[] {userId}));

    return userFollowRepository.findByFollows(profile, PageRequest.of(page, pageSize)).getContent();
  }

  @Override
  public List<UserFollow> getFollowed(
      @ValidUserId String userId, @ValidPage int page, @ValidPaging int pageSize) {

    UserProfile profile =
        repository
            .findById(UUID.fromString(userId))
            .orElseThrow(
                () -> new ResourceNotFoundException(USER_NOT_FOUND, new Object[] {userId}));

    return userFollowRepository
        .findByFollowed(profile, PageRequest.of(page, pageSize))
        .getContent();
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Override
  public UserFollow follow(String jwtToken, @ValidUserId String userId) {
    UserProfile currentUser = getLoggedInUser(jwtToken);

    if (currentUser.getId().toString().equals(userId)) {
      throw new LogicException(CANNOT_FOLLOW_SELF, null);
    }

    UserProfile followedUser =
        repository
            .findById(UUID.fromString(userId))
            .orElseThrow(
                () -> new ResourceNotFoundException(USER_NOT_FOUND, new Object[] {userId}));

    Optional<UserFollow> follow =
        userFollowRepository.findByFollowsAndFollowed(currentUser, followedUser);

    if (follow.isPresent()) {
      return follow.get();
    }

    UserFollow userFollow = new UserFollow();
    userFollow.setFollows(currentUser);
    userFollow.setFollowed(followedUser);

    userFollowRepository.save(userFollow);

    currentUser.setFollowsCount(currentUser.getFollowsCount() + 1);
    followedUser.setFollowedCount(followedUser.getFollowedCount() + 1);

    repository.save(currentUser);
    repository.save(followedUser);
    userFollow = userFollowRepository.save(userFollow);

    followProducerService.sendMessage(userFollow);
    return userFollow;

  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Override
  public void unfollow(String jwtToken, @ValidUserId String userId) {
    UserProfile currentUser = getLoggedInUser(jwtToken);

    if (currentUser.getId().toString().equals(userId)) {
      throw new LogicException(CANNOT_FOLLOW_SELF, null);
    }

    UserProfile followedUser =
        repository
            .findById(UUID.fromString(userId))
            .orElseThrow(
                () -> new ResourceNotFoundException(USER_NOT_FOUND, new Object[] {userId}));

    Optional<UserFollow> follow =
        userFollowRepository.findByFollowsAndFollowed(currentUser, followedUser);

    if (follow.isEmpty()) {
      return;
    }

    currentUser.setFollowsCount(currentUser.getFollowsCount() - 1);
    followedUser.setFollowedCount(followedUser.getFollowedCount() - 1);

    repository.save(currentUser);
    repository.save(followedUser);
    userFollowRepository.delete(follow.get());
  }

  @Override
  public UserProfile updateProfile(
      String jwtToken, @ValidUpdateUserProfileDto UpdateUserProfileDto profileDto) {
    UserProfile currentUser = getLoggedInUser(jwtToken);
    currentUser.setFirstName(profileDto.firstName());
    currentUser.setLastName(profileDto.lastName());
    currentUser.setProfilePictureUrl(profileDto.profilePictureUrl());
    currentUser.setUpdatedAt(OffsetDateTime.now());
    return repository.save(currentUser);
  }

  private UserProfile createUserProfileFromJwtTokenInfo(JwtTokenInfo tokenInfo) {
    UserProfile profile = new UserProfile();

    profile.setAuthenticationProviderId(tokenInfo.authenticationProviderId());
    profile.setFirstName(tokenInfo.firstName());
    profile.setLastName(tokenInfo.lastName());
    profile.setEmail(tokenInfo.email());
    profile.setProfilePictureUrl(tokenInfo.profilePictureUrl());

    return profile;
  }
}
