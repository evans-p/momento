package gr.evansp.momento.service;

import gr.evansp.momento.beans.JwtTokenInfo;
import gr.evansp.momento.exception.LogicException;

public interface JwtService {
  /**
   * extracts {@link JwtTokenInfo} from A JWT token.
   * Throws {@link LogicException} if any of the {@link JwtTokenInfo}
   * do not exist within the token.
   *
   * @param token
   * 		JWT token.
   * @return {@link JwtTokenInfo}
   */
  JwtTokenInfo extractUserProfileInfo(String token);
}
