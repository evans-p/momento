package gr.evansp.momento.service;


import gr.evansp.momento.dto.UploadPostDataDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for managing post-related operations.
 *
 * @author Momento Development Team
 * @since 1.0
 */
public interface PostManagementService {

    /**
     * Uploads a new post with the provided media file and metadata.
     *
     * @param file                 the media file to upload
     * @param postData             the post metadata (description, location, tagged users)
     * @param authorizationHeader  the authorization header containing the user's JWT token
     */
    void uploadPost(MultipartFile file, UploadPostDataDto postData, String authorizationHeader);
}
