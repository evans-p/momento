package gr.evansp.momento.beans;

import com.auth0.jwt.JWT;

/**
 * The extracted info from a {@link JWT} token.
 *
 * @param authenticationProviderId
 * 		authenticationProviderId
 * @param firstName
 * 		firstName
 * @param lastName
 * 		lastName
 * @param email
 * 		email
 * @param profilePictureUrl
 * 		profilePictureUrl
 */
public record JwtTokenInfo(
    String authenticationProviderId,
    String firstName,
    String lastName,
    String email,
    String profilePictureUrl) {}
