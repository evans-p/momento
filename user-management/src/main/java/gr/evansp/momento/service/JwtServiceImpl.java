package gr.evansp.momento.service;

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

  private static final String AUTHENTICATION_PROVIDER_ID_CLAIM = "sub";

  private static final String FIRST_NAME_CLAIM = "given_name";

  private static final String LAST_NAME_CLAIM = "family_name";

  private static final String EMAIL_CLAIM = "email";

  private static final String PROFILE_PICTURE_URL_CLAIM = "picture";

  @Override
  public JwtTokenInfo extractUserProfileInfo(String token) {
    try {
      DecodedJWT decodedJWT = JWT.decode(token);
      return new JwtTokenInfo(
          getClaimAsString(decodedJWT, AUTHENTICATION_PROVIDER_ID_CLAIM),
          getClaimAsString(decodedJWT, FIRST_NAME_CLAIM),
          getClaimAsString(decodedJWT, LAST_NAME_CLAIM),
          getClaimAsString(decodedJWT, EMAIL_CLAIM),
          getClaimAsString(decodedJWT, PROFILE_PICTURE_URL_CLAIM));
    } catch (JWTDecodeException e) {
      throw new LogicException(INVALID_TOKEN, null);
    }
  }

  private String getClaimAsString(DecodedJWT decodedJWT, String claimName) {
    Claim claim = decodedJWT.getClaim(claimName);
    if (claim.isMissing()) {
      throw new LogicException(INVALID_TOKEN, null);
    }
    return claim.asString();
  }
}
