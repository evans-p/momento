package gr.evansp.momento.service;

import gr.evansp.momento.client.CDNClient;
import gr.evansp.momento.client.UserClient;
import gr.evansp.momento.dto.AssetDto;
import gr.evansp.momento.dto.UploadPostDataDto;
import gr.evansp.momento.dto.UserProfileDto;
import gr.evansp.momento.model.Post;
import gr.evansp.momento.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
public class PostManagementServiceImpl implements PostManagementService {

    @Autowired
    UserClient userClient;

    @Autowired
    CDNClient cdnClient;

    @Autowired
    PostRepository postRepository;

    @Value("${cdn.client.url}")
    private String cdnClientUrl;

    @Override
    public void uploadPost(MultipartFile file, UploadPostDataDto postData, String authorizationHeader) {

        UserProfileDto userProfile = userClient.getLoggedInUserProfile(authorizationHeader);

        AssetDto asset = cdnClient.upload(file).getBody();

        Post post = new Post();

        post.setOwnerId(UUID.fromString(userProfile.id()));
        post.setImageUrl(cdnClientUrl+ "cdn/v1/assets/"+asset.fileName());
        post.setLatitude(postData.geolocationDto().latitude());
        post.setLongitude(postData.geolocationDto().longitude());

        postRepository.save(post);
    }
}
