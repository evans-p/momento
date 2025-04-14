package gr.evansp.momento.service;

import static gr.evansp.momento.constant.ExceptionConstants.INVALID_USER_ID;
import static gr.evansp.momento.constant.ExceptionConstants.USER_ALREADY_REGISTERED;
import static gr.evansp.momento.constant.ExceptionConstants.USER_NOT_FOUND;

import gr.evansp.momento.beans.JwtTokenInfo;
import gr.evansp.momento.exception.LogicException;
import gr.evansp.momento.model.UserProfile;
import gr.evansp.momento.repository.UserProfileRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementServiceImpl implements UserManagementService {

  private static final String USER_ID_REGEX =
      "^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}$";

  @Autowired JwtService jwtService;

  @Autowired UserProfileRepository repository;

  @Transactional
  @Override
  public UserProfile register(String jwtToken) {
    JwtTokenInfo tokenInfo = jwtService.extractUserProfileInfo(jwtToken);

    Optional<UserProfile> fetchedProfile =
        repository.findByAuthenticationProviderId(tokenInfo.authenticationProviderId());

    if (fetchedProfile.isPresent()) {
      throw new LogicException(USER_ALREADY_REGISTERED, null);
    }

    //TODO: handle exceptions here.
    return repository.save(createUserProfileFromJwtTokenInfo(tokenInfo));
  }

  @Override
  public UserProfile getUser(String userId) {
    validateUserId(userId);

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

  private void validateUserId(String userId) {
    if (userId == null || userId.isBlank()) {
      throw new LogicException(INVALID_USER_ID, new Object[] {userId});
    }
    Pattern pattern = Pattern.compile(USER_ID_REGEX);
    Matcher matcher = pattern.matcher(userId);

    if (!matcher.matches()) {
      throw new LogicException(INVALID_USER_ID, new Object[] {userId});
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
