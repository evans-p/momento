package gr.evansp.momento.controller;

import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.dto.UserFollowDto;
import gr.evansp.momento.dto.UserProfileDto;
import gr.evansp.momento.service.UserManagementService;
import gr.evansp.momento.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users/v1/")
public class UserManagementController implements UserManagementControllerAPI {

  /** {@link UserManagementService}. */
  private final UserService service;

  @Autowired
  public UserManagementController(UserService service) {
    this.service = service;
  }

  @PostMapping("register")
  public ResponseEntity<UserProfileDto> registerUser(
      @RequestHeader("Authorization") String authorizationHeader) {

    String jwtToken = extractJwtToken(authorizationHeader);

    return new ResponseEntity<>(service.register(jwtToken), HttpStatus.OK);
  }

  @GetMapping("")
  public ResponseEntity<UserProfileDto> getLoggedInUser(
      @RequestHeader("Authorization") String authorizationHeader) {

    String jwtToken = extractJwtToken(authorizationHeader);

    return new ResponseEntity<>(service.getLoggedInUser(jwtToken), HttpStatus.OK);
  }

  @GetMapping("followers")
  public ResponseEntity<List<UserProfileDto>> getLoggedInUserFollowers(
      @RequestHeader("Authorization") String authorizationHeader,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    String jwtToken = extractJwtToken(authorizationHeader);

    return new ResponseEntity<>(
        service.getLoggedInUserFollowed(jwtToken, page, size), HttpStatus.OK);
  }

  @GetMapping("follows")
  public ResponseEntity<List<UserProfileDto>> getLoggedInUserFollows(
      @RequestHeader("Authorization") String authorizationHeader,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    String jwtToken = extractJwtToken(authorizationHeader);

    return new ResponseEntity<>(
        service.getLoggedInUserFollows(jwtToken, page, size), HttpStatus.OK);
  }

  @PutMapping("")
  public ResponseEntity<UserProfileDto> updateUser(
      @RequestBody UpdateUserProfileDto userProfileDto,
      @RequestHeader("Authorization") String authorizationHeader) {
    String jwtToken = extractJwtToken(authorizationHeader);

    return new ResponseEntity<>(service.updateProfile(jwtToken, userProfileDto), HttpStatus.OK);
  }

  @GetMapping("{userId}")
  public ResponseEntity<UserProfileDto> getUser(@PathVariable String userId) {

    return new ResponseEntity<>(service.getUser(userId), HttpStatus.OK);
  }

  @GetMapping("{userId}/followers")
  public ResponseEntity<List<UserProfileDto>> getFollowers(
      @PathVariable String userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    return new ResponseEntity<>(service.getFollowed(userId, page, size), HttpStatus.OK);
  }

  @GetMapping("{userId}/follows")
  public ResponseEntity<List<UserProfileDto>> getFollows(
      @PathVariable String userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    return new ResponseEntity<>(service.getFollows(userId, page, size), HttpStatus.OK);
  }

  @PostMapping("{userId}/follow")
  public ResponseEntity<UserFollowDto> follow(
      @PathVariable String userId, @RequestHeader("Authorization") String authorizationHeader) {
    String jwtToken = extractJwtToken(authorizationHeader);

    return new ResponseEntity<>(service.follow(jwtToken, userId), HttpStatus.OK);
  }

  @PostMapping("{userId}/unfollow")
  public ResponseEntity<Void> unfollow(
      @PathVariable String userId, @RequestHeader("Authorization") String authorizationHeader) {
    String jwtToken = extractJwtToken(authorizationHeader);
    service.unfollow(jwtToken, userId);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private String extractJwtToken(String authorizationHeader) {
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.substring(7);
    }
    return null;
  }
}
