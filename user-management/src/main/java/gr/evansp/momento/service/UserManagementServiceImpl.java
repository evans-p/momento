package gr.evansp.momento.service;

import java.util.Optional;

import gr.evansp.momento.beans.JwtTokenInfo;
import gr.evansp.momento.exception.LogicException;
import gr.evansp.momento.model.UserProfile;
import gr.evansp.momento.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gr.evansp.momento.constant.ExceptionConstants.USER_ALREADY_REGISTERED;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	@Autowired
	JwtService jwtService;

	@Autowired
	UserProfileRepository repository;

	@Transactional
	@Override
	public UserProfile register(String jwtToken) {
		JwtTokenInfo tokenInfo = jwtService.extractUserProfileInfo(jwtToken);

		Optional<UserProfile> fetchedProfile = repository.findByAuthenticationProviderId(tokenInfo.authenticationProviderId());

		if (fetchedProfile.isPresent()) {
			throw new LogicException(USER_ALREADY_REGISTERED, null);
		}

		return repository.save(createUserProfileFromJwtTokenInfo(tokenInfo));
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
