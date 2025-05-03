package gr.evansp.momento.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.model.UserFollow;
import gr.evansp.momento.model.UserProfile;
import gr.evansp.momento.repository.UserFollowRepository;
import gr.evansp.momento.service.UserManagementService;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Integration tests for {@link UserManagementController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TestUserManagementController extends AbstractIntegrationTest {

  private static final String BASE_URI = "/users/v1/";

  /**
   * {@link MockMvc}.
   */
  @Autowired MockMvc mockMvc;

  /**
   * {@link UserManagementService}.
   */
  @Autowired UserManagementService service;

  /**
   * {@link UserFollowRepository}.
   */
  @Autowired UserFollowRepository userFollowRepository;

  /**
   * {@link ObjectMapper}.
   */
  private static ObjectMapper objectMapper;

  @BeforeAll
  public static void setup() {
    objectMapper = new ObjectMapper();
    // Register JavaTimeModule to handle Java 8 date/time types
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void testFaultyUrl_english() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/faulty"))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json(
                    "{\"messages\":{\"resource.not.found\":\"No content found at the specified URL.\"}}"));
  }

  @Test
  public void testFaultyUrl_greek() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/faulty").header("Accept-Language", "el-GR"))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json(
                    "{\"messages\":{\"resource.not.found\":\"Δε βρέθηκε περιεχόμενο στο συγκεκριμένο URL.\"}}"));
  }

  @Test
  public void testFaultyUrl_french() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/faulty").header("Accept-Language", "fr-FR"))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json(
                    "{\"messages\":{\"resource.not.found\":\"No content found at the specified URL.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#registerUser(String)}
   * @throws Exception Exception
   */
  @Test
  public void testRegisterUser_noToken() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(BASE_URI + "register"))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json(
                    "{\"messages\":{\"authorization.header.not.present\":\"Required request header 'Authorization' is not present.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#registerUser(String)}
   * @throws Exception Exception
   */
  @Test
  public void testRegisterUser_emptyToken() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(BASE_URI + "register").header("Authorization", ""))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("{\"messages\":{\"invalid.token\":\"Invalid Token.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#registerUser(String)}
   * @throws Exception Exception
   */
  @Test
  public void testRegisterUser_ok() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + "register")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Test for {@link UserManagementController#registerUser(String)}
   * @throws Exception Exception
   */
  @Test
  public void testRegisterUser_userAlreadyExists() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + "register")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + "register")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json("{\"messages\":{\"user.already.registered\":\"User already exists.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#getLoggedInUser(String)}
   * @throws Exception Exception
   */
  @Test
  public void testGetLoggedInUser_userNotFound() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URI)
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(404))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("{\"messages\":{\"user.not.found\":\"User not found .\"}}"));
  }

  /**
   * Test for {@link UserManagementController#getLoggedInUser(String)}
   * @throws Exception Exception
   */
  @Test
  public void testGetLoggedInUser_ok() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + "register")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URI)
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Test for {@link UserManagementController#getLoggedInUserFollowers(String, int, int)}.
   * @throws Exception Exception
   */
  @Test
  public void testGetLoggedInUserFollowers_invalidPageAndPaging() throws Exception {

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + "register")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URI + "followers?page=-1&size=-1")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(422))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json(
                    "{\"messages\":{\"invalid.paging\":\"Faulty paging value.\",\"invalid.page\":\"Faulty page value.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#getLoggedInUserFollowers(String, int, int)}.
   * @throws Exception Exception
   */
  @Test
  public void testGetLoggedInUserFollowers_emptyPage() throws Exception {

    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = new UserFollow();
    follow.setFollows(profile1);
    follow.setFollowedBy(profile2);

    userFollowRepository.save(follow);

    UserFollow follow2 = new UserFollow();
    follow2.setFollows(profile2);
    follow2.setFollowedBy(profile1);

    userFollowRepository.save(follow2);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URI + "followers?page=2&size=20")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("[]"));
  }

  /**
   * Test for {@link UserManagementController#getLoggedInUserFollowers(String, int, int)}.
   * @throws Exception Exception
   */
  @Test
  public void testGetLoggedInUserFollowers_defaultPagingValues() throws Exception {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = new UserFollow();
    follow.setFollows(profile1);
    follow.setFollowedBy(profile2);

    userFollowRepository.save(follow);

    UserFollow follow2 = new UserFollow();
    follow2.setFollows(profile2);
    follow2.setFollowedBy(profile1);

    userFollowRepository.save(follow2);

    String result =
        "[{\"id\":\"FOLLOW_ID\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"profilePictureUrl\":\"https://graph.facebook.com/123456789012345/picture\",\"followsCount\":0,\"followedByCount\":0}]"
            .replace("FOLLOW_ID", profile2.getId().toString());

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URI + "followers")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(result));
  }

  /**
   * Test for {@link UserManagementController#getLoggedInUserFollows(String, int, int)}.
   * @throws Exception Exception
   */
  @Test
  public void testGetLoggedInUserFollows_defaultPagingValues() throws Exception {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = new UserFollow();
    follow.setFollows(profile1);
    follow.setFollowedBy(profile2);

    userFollowRepository.save(follow);

    UserFollow follow2 = new UserFollow();
    follow2.setFollows(profile2);
    follow2.setFollowedBy(profile1);

    userFollowRepository.save(follow2);

    String result =
        "[{\"id\":\"FOLLOW_ID\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"profilePictureUrl\":\"https://graph.facebook.com/123456789012345/picture\",\"followsCount\":0,\"followedByCount\":0}]"
            .replace("FOLLOW_ID", profile2.getId().toString());

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URI + "follows")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(result));
  }

  /**
   * Test for {@link UserManagementController#updateUser(UpdateUserProfileDto, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUpdateUser_emptyHeaderNoBodyNoContentType() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.put(BASE_URI).header("Authorization", ""))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content().json("{\"messages\":{\"faulty.message.body\":\"Faulty Request Body.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#updateUser(UpdateUserProfileDto, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUpdateUser_noHeaderNoBody() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.put(BASE_URI))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content().json("{\"messages\":{\"faulty.message.body\":\"Faulty Request Body.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#updateUser(UpdateUserProfileDto, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUpdateUser_emptyHeaderEmptyBody() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.put(BASE_URI).header("Authorization", "").content(""))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content().json("{\"messages\":{\"faulty.message.body\":\"Faulty Request Body.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#updateUser(UpdateUserProfileDto, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUpdateUser_emptyHeaderEmptyContent() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.put(BASE_URI).header("Authorization", "").content("{}"))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json(
                    "{\"messages\":{\"media.type.not.supported\":\"The content type is not supported.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#updateUser(UpdateUserProfileDto, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUpdateUser_emptyContent() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URI)
                .header("Authorization", VALID_GOOGLE_TOKEN)
                .content(
                    objectMapper.writeValueAsString(new UpdateUserProfileDto(null, null, null)))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(422))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json(
                    "{\"messages\":{\"last.name.cannot.be.empty\":\"The field cannot be empty.\",\"first.name.cannot.be.empty\":\"The field cannot be empty.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#updateUser(UpdateUserProfileDto, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUpdateUser_userNotFound() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URI)
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN)
                .content(
                    objectMapper.writeValueAsString(new UpdateUserProfileDto("John", "John", null)))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("{\"messages\":{\"user.not.found\":\"User not found .\"}}"));
  }

  /**
   * Test for {@link UserManagementController#updateUser(UpdateUserProfileDto, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUpdateUser_invalidCharacters() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + "register")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URI)
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN)
                .content(
                    objectMapper.writeValueAsString(
                        new UpdateUserProfileDto("<b>John<\b>", "John", null)))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(422))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json(
                    "{\"messages\":{\"first.name.contains.invalid.characters\":\"The field contains invalid characters.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#updateUser(UpdateUserProfileDto, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUpdateUser_ok() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + "register")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URI)
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN)
                .content(
                    objectMapper.writeValueAsString(new UpdateUserProfileDto("John", "John", null)))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Test for {@link UserManagementController#getUser(String)}
   * @throws Exception Exception
   */
  @Test
  public void testGetUser_ok() throws Exception {
    UserProfile profile = service.register(VALID_GOOGLE_TOKEN);
    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URI + profile.getId().toString()))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Test for {@link UserManagementController#getUser(String)}.
   * @throws Exception Exception
   */
  @Test
  public void testGetUser_faultyUserId() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URI + "profile.getId().toString()"))
        .andExpect(status().is(422))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("{\"messages\":{\"invalid.user.id\":\"Invalid user id.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#getFollowers(String, int, int)}.
   * @throws Exception Exception
   */
  @Test
  public void testGetFollowers_ok() throws Exception {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = new UserFollow();
    follow.setFollows(profile1);
    follow.setFollowedBy(profile2);

    userFollowRepository.save(follow);

    UserFollow follow2 = new UserFollow();
    follow2.setFollows(profile2);
    follow2.setFollowedBy(profile1);

    userFollowRepository.save(follow2);

    String response1 =
        "[{\"id\":\"GOOGLE_ID\",\"firstName\":\"Βαγγέλης\",\"lastName\":\"Πουλάκης\",\"profilePictureUrl\":\"https://lh3.googleusercontent.com/a/ACg8ocItjtnWNX4BhkKLDvOV9ChIYx5_r_UzSgftgwIp6TmyZrloVJM=s96-c\",\"followsCount\":0,\"followedByCount\":0}]"
            .replace("GOOGLE_ID", profile1.getId().toString());

    String response2 =
        "[{\"id\":\"FACEBOOK_ID\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"profilePictureUrl\":\"https://graph.facebook.com/123456789012345/picture\",\"followsCount\":0,\"followedByCount\":0}]"
            .replace("FACEBOOK_ID", profile2.getId().toString());
    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URI + profile2.getId() + "/followers"))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(response1));

    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URI + profile1.getId() + "/followers"))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(response2));
  }

  /**
   * Test for {@link UserManagementController#getFollows(String, int, int)}.
   * @throws Exception Exception
   */
  @Test
  public void testGetFollows_ok() throws Exception {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    UserFollow follow = new UserFollow();
    follow.setFollows(profile1);
    follow.setFollowedBy(profile2);

    userFollowRepository.save(follow);

    UserFollow follow2 = new UserFollow();
    follow2.setFollows(profile2);
    follow2.setFollowedBy(profile1);

    userFollowRepository.save(follow2);

    String response1 =
        "[{\"id\":\"GOOGLE_ID\",\"firstName\":\"Βαγγέλης\",\"lastName\":\"Πουλάκης\",\"profilePictureUrl\":\"https://lh3.googleusercontent.com/a/ACg8ocItjtnWNX4BhkKLDvOV9ChIYx5_r_UzSgftgwIp6TmyZrloVJM=s96-c\",\"followsCount\":0,\"followedByCount\":0}]"
            .replace("GOOGLE_ID", profile1.getId().toString());

    String response2 =
        "[{\"id\":\"FACEBOOK_ID\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"profilePictureUrl\":\"https://graph.facebook.com/123456789012345/picture\",\"followsCount\":0,\"followedByCount\":0}]"
            .replace("FACEBOOK_ID", profile2.getId().toString());
    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URI + profile2.getId() + "/follows"))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(response1));

    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URI + profile1.getId() + "/follows"))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(response2));
  }

  /**
   * Test for {@link UserManagementController#follow(String, String)} .
   * @throws Exception Exception
   */
  @Test
  public void testFollow_followSelf() throws Exception {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + profile1.getId() + "/follow")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json("{\"messages\":{\"cannot.follow.self\":\"You can't follow yourself.\"}}"));

    assertEquals(0, userFollowRepository.findAll().size());
  }

  /**
   * Test for {@link UserManagementController#follow(String, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testFollow_ok() throws Exception {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    assertEquals(0, userFollowRepository.findAll().size());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + profile2.getId() + "/follow")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    List<UserFollow> follows = userFollowRepository.findAll();

    assertEquals(1, follows.size());
    assertEquals(follows.getFirst().getFollowedBy(), profile2);
    assertEquals(follows.getFirst().getFollows(), profile1);
  }

  /**
   * Test for {@link UserManagementController#unfollow(String, String)}
   * @throws Exception Exception
   */
  @Test
  public void testUnfollow_followSelf() throws Exception {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + profile1.getId() + "/unfollow")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(400))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            content()
                .json("{\"messages\":{\"cannot.follow.self\":\"You can't follow yourself.\"}}"));
  }

  /**
   * Test for {@link UserManagementController#unfollow(String, String)}.
   * @throws Exception Exception
   */
  @Test
  public void testUnfollow_ok() throws Exception {
    UserProfile profile1 = service.register(VALID_GOOGLE_TOKEN);
    UserProfile profile2 = service.register(VALID_FACEBOOK_TOKEN);

    assertEquals(0, userFollowRepository.findAll().size());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + profile2.getId() + "/follow")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    List<UserFollow> follows = userFollowRepository.findAll();

    assertEquals(1, follows.size());
    assertEquals(follows.getFirst().getFollowedBy(), profile2);
    assertEquals(follows.getFirst().getFollows(), profile1);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URI + profile2.getId() + "/unfollow")
                .header("Authorization", "Bearer " + VALID_GOOGLE_TOKEN))
        .andExpect(status().is(200));

    assertEquals(0, userFollowRepository.findAll().size());
  }
}
