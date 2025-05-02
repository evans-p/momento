package gr.evansp.momento.dto;

import gr.evansp.momento.model.UserProfile;

/**
 * Data transfer object for updating {@link UserProfile}.
 *
 * @param firstName
 * 		firstName
 * @param lastName
 * 		lastName
 * @param profilePictureUrl
 * 		profilePictureUrl
 */
public record UpdateUserProfileDto(String firstName, String lastName, String profilePictureUrl) {}
