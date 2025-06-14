package gr.evansp.momento.controller;

import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.dto.UserFollowDto;
import gr.evansp.momento.dto.UserProfileDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserManagementControllerAPI {

  ResponseEntity<UserProfileDto> registerUser(
      @RequestHeader("Authorization") String authorizationHeader);


  ResponseEntity<UserProfileDto> getLoggedInUser(
      @RequestHeader("Authorization") String authorizationHeader);

  ResponseEntity<List<UserProfileDto>> getLoggedInUserFollowers(
      @RequestHeader("Authorization") String authorizationHeader,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size);

  ResponseEntity<List<UserProfileDto>> getLoggedInUserFollows(
      @RequestHeader("Authorization") String authorizationHeader,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size);

  ResponseEntity<UserProfileDto> updateUser(
      @RequestBody UpdateUserProfileDto userProfileDto,
      @RequestHeader("Authorization") String authorizationHeader);

  ResponseEntity<UserProfileDto> getUser(@PathVariable String userId);

  ResponseEntity<List<UserProfileDto>> getFollows(
      @PathVariable String userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size);

  ResponseEntity<UserFollowDto> follow(
      @PathVariable String userId, @RequestHeader("Authorization") String authorizationHeader);

  ResponseEntity<Void> unfollow(
      @PathVariable String userId, @RequestHeader("Authorization") String authorizationHeader);
}
