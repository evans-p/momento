package gr.evansp.momento.dto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Data Transfer Object for uploading a new post to the platform.
 * <p>
 * This record encapsulates all the information required to create a new social media post,
 * including the media file, textual description, location data, and user tags.
 * The actual post author is derived from the authentication context and is not included in this DTO.
 * </p>
 *
 * @param description    The textual content or caption for the post. Can be {@code null} or empty
 *                       if the post is image-only without a description.
 * @param file           The media file to be uploaded (image). Must be a valid image file format
 *                       (e.g., JPEG, PNG, WebP). Cannot be {@code null}.
 * @param geolocationDto The geographical location information associated with the post.
 *                       Can be {@code null} if the user chooses not to share location data.
 * @param taggedUsers    A list of user IDs representing users tagged in this post.
 *                       Can be {@code null} or empty if no users are tagged.
 *                       Tagged users may receive notifications and the post may appear in their profile.
 *
 * @author Momento Development Team
 * @since 1.0
 * @see GeolocationDto
 */
public record UploadPostDto(
    String description,
    MultipartFile file,
    GeolocationDto geolocationDto,
    List<String> taggedUsers) {}
