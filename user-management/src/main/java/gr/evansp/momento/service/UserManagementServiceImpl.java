package gr.evansp.momento.service;

import static gr.evansp.momento.constant.ExceptionConstants.INVALID_PAGING;
import static gr.evansp.momento.constant.ExceptionConstants.USER_ALREADY_REGISTERED;
import static gr.evansp.momento.constant.ExceptionConstants.USER_NOT_FOUND;

import gr.evansp.momento.annotation.ValidUserId;
import gr.evansp.momento.beans.JwtTokenInfo;
import gr.evansp.momento.exception.LogicException;
import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import gr.evansp.momento.repository.UserFollowRepository;
import gr.evansp.momento.repository.UserProfileRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Implementation of {@link UserManagementService}.
 */
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

  @Transactional
  @Override
  public UserProfile register(String jwtToken) {
    JwtTokenInfo tokenInfo = jwtService.extractUserProfileInfo(jwtToken);

    Optional<UserProfile> fetchedProfile =
        repository.findByAuthenticationProviderId(tokenInfo.authenticationProviderId());

    if (fetchedProfile.isPresent()) {
      throw new LogicException(USER_ALREADY_REGISTERED, null);
    }

    // TODO: handle exceptions here.
    return repository.save(createUserProfileFromJwtTokenInfo(tokenInfo));
  }

  @Override
  public UserProfile getUser(@ValidUserId String userId) {
    return repository
        .findById(UUID.fromString(userId))
        .orElseThrow(() -> new LogicException(USER_NOT_FOUND, null));
  }

  @Override
  public UserProfile getLoggedInUser(String jwtToken) {
    JwtTokenInfo tokenInfo = jwtService.extractUserProfileInfo(jwtToken);

    return repository
        .findByAuthenticationProviderId(tokenInfo.authenticationProviderId())
        .orElseThrow(() -> new LogicException(USER_NOT_FOUND, null));
  }

  @Override
  public List<UserFollow> getFollows(@ValidUserId String userId, int page, int pageSize) {
    validatePaging(page, pageSize);

    UserProfile profile =
        repository
            .findById(UUID.fromString(userId))
            .orElseThrow(() -> new LogicException(USER_NOT_FOUND, null));

    return userFollowRepository.findByFollows(profile, PageRequest.of(page, pageSize)).getContent();
  }

  // TODO: Move to validator
  private void validatePaging(int page, int pageSize) {
    if (page < 0 || pageSize <= 0) {
      throw new LogicException(INVALID_PAGING, null);
    }
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
