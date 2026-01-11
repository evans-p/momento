package gr.evansp.momento.client;

import gr.evansp.momento.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


/**
 * Feign client interface for communicating with the User Management service.
 * <p>
 * This client provides methods to retrieve user profile information from the
 * external user management microservice. The base URL is configured via the
 * {@code user.management.client.url} property.
 * </p>
 *
 * @see UserProfileDto
 */
@FeignClient(name = "user-management-client", url = "${user.management.client.url}")
public interface UserClient {

    /**
     * Retrieves the profile information for the currently logged-in user.
     * <p>
     * This method makes a GET request to the root endpoint of the user management
     * service, passing the authorization token to identify and authenticate the user.
     * </p>
     *
     * @param authorizationHeader the authorization header containing the bearer token
     *                           or other authentication credentials (e.g., "Bearer eyJhbGc...")
     * @return a {@link UserProfileDto} containing the logged-in user's profile information
     * @throws feign.FeignException if the request fails due to network issues,
     *                             authentication errors, or server-side problems
     */
    @GetMapping("/")
    UserProfileDto getLoggedInUserProfile(@RequestHeader("Authorization") String authorizationHeader);
}
