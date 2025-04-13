package gr.evansp.momento.service;

import java.util.Set;

import static gr.evansp.momento.constant.ExceptionConstants.INVALID_TOKEN;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import gr.evansp.momento.beans.JwtTokenInfo;
import gr.evansp.momento.exception.LogicException;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

	private static final Set<String> AUTHENTICATION_PROVIDER_ID_CLAIM = Set.of("sub");

	private static final Set<String> FIRST_NAME_CLAIM = Set.of("given_name", "first_name", "firstName");

	private static final Set<String> LAST_NAME_CLAIM = Set.of("family_name", "last_name", "lastName");

	private static final Set<String> EMAIL_CLAIM = Set.of("email", "preferred_username");

	private static final Set<String> PROFILE_PICTURE_URL_CLAIM = Set.of("picture");

	@Override
	public JwtTokenInfo extractUserProfileInfo(String token) {
		try {
			DecodedJWT decodedJWT = JWT.decode(token);
			return new JwtTokenInfo(
					getClaimAsString(decodedJWT, AUTHENTICATION_PROVIDER_ID_CLAIM),
					getClaimAsString(decodedJWT, FIRST_NAME_CLAIM),
					getClaimAsString(decodedJWT, LAST_NAME_CLAIM),
					getClaimAsString(decodedJWT, EMAIL_CLAIM),
					getClaimAsStringNoException(decodedJWT, PROFILE_PICTURE_URL_CLAIM));
		} catch (JWTDecodeException e) {
			throw new LogicException(INVALID_TOKEN, null);
		}
	}

	/**
	 * Parse the {@link DecodedJWT} and fetches the desired claim, based
	 * on the claimName parameter. If claim is missing from token,
	 * returns null
	 *
	 * @param decodedJWT
	 *        {@link DecodedJWT}.
	 * @param claimNames
	 * 		name of the {@link Claim} in token.
	 * @return Claim value as string.
	 */
	private String getClaimAsStringNoException(DecodedJWT decodedJWT, Set<String> claimNames) {
      for (String claimName : claimNames) {
        Claim claim = decodedJWT.getClaim(claimName);
        if (!claim.isMissing()) {
          return claim.asString();
        }
      }
      return null;
	}

	/**
	 * Parse the {@link DecodedJWT} and fetches the desired claim, based
	 * on the claimName parameter. If claim is missing from token,
	 * throws {@link LogicException}.
	 *
	 * @param decodedJWT
	 *        {@link DecodedJWT}.
	 * @param claimNames
	 * 		name of the {@link Claim} in token.
	 * @return Claim value as string.
	 */
	private String getClaimAsString(DecodedJWT decodedJWT, Set<String> claimNames) {
      for (String claimName : claimNames) {
        Claim claim = decodedJWT.getClaim(claimName);
        if (!claim.isMissing()) {
          return claim.asString();
        }
      }
      throw new LogicException(INVALID_TOKEN, null);
	}
}
