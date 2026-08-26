package gr.evansp.momento.dto;

/**
 * Data transfer object for User
 *
 * @param firstName         firstName
 * @param lastName          lastName
 * @param profilePictureUrl profilePictureUrl
 */
public record UserProfileDto(
        String id,
        String firstName,
        String lastName,
        String profilePictureUrl,
        Long followsCount,
        Long followedCount) {

}